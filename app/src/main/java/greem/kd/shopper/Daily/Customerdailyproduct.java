package greem.kd.shopper.Daily;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import greem.kd.shopper.Config.StaticConfig;
import greem.kd.shopper.R;

/**
 * Created by kd on 31/12/17.
 */

public class Customerdailyproduct extends AppCompatActivity  {
    public TableLayout tl;
    private Float remainamt;
    private Float totalamt;
    private Float todayamt;
    private TextView remainamount;
    private TextView pre_lbl;
    private TextView today_lbl;
    private float FinalAdditionAmt =0;
    private TextView totalamount;
    private String today;
    private String total;
    private String remain;
    DecimalFormat df = new DecimalFormat("#.000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.custdailyinfo);
        Bundle extras = getIntent().getExtras();
        final String CustId=getIntent().getStringExtra("Custid");
        today=getIntent().getStringExtra("today");
        String pre=getIntent().getStringExtra("pre");
        total=getIntent().getStringExtra("total");
        remain=getIntent().getStringExtra("remain");

        final String[][] customer_daily = (String[][]) extras.getSerializable("custDaily");

        Log.d("kd.greem",""+customer_daily[0].length);
        //Log.d("kd.greem",""+customer_daily[0][0].length());

        TextView lblDate = (TextView) findViewById(R.id.lbl_date);
        String formattedDate = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        lblDate.setText(formattedDate);

        String route=customer_daily[StaticConfig.daily_route][0];
        String trip=customer_daily[StaticConfig.daily_trip][0];
        String custName =customer_daily[StaticConfig.daily_Custname][0];
        String custphone =customer_daily[StaticConfig.daily_Contact][0];

        ((TextView)findViewById(R.id.txt_route)).setText(route);
        ((TextView)findViewById(R.id.txt_trip)).setText(trip);
        ((TextView)findViewById(R.id.txt_custname_1)).setText(custName);

        today_lbl=(TextView)findViewById(R.id.txt_today);
        today_lbl.setText( today);
        pre_lbl=(TextView)findViewById(R.id.txt_pre);
        pre_lbl.setText(   pre);

        tl = (TableLayout) findViewById(R.id.table1);

        // Create the table row


        totalamount=(TextView) findViewById(R.id.lbl_totalAmount);
        totalamount.setText(total);

        remainamount = (TextView) findViewById(R.id.lbl_remainAmount);
        remainamount.setText(remain);


        totalamt = Float.valueOf(totalamount.getText().toString());
        todayamt =Float.valueOf(today);
        remainamt =Float.valueOf(remain);

        EditText paid= (EditText) findViewById(R.id.txt_paidamount);
        paid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0) {
                    remainamount.setText(String.valueOf(totalamt - Float.valueOf(s.toString())));
                }
            }
        });
        setProducts(customer_daily);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do save page here
                for(int c=1; c<tl.getChildCount();c++){
                    TableRow row = (TableRow)tl.getChildAt(c);
                    Log.d("kd.greem"," table row childs are "+row.getChildCount());

                    EditText qty1 = (EditText) row.getChildAt(1);
                    if(qty1.getText().length()<=0){
                        qty1.setText("0");
                    }
                    TextView hide= (TextView) row.getChildAt(2);
                    String bid = hide.getText().toString();

                    for(int i=0;i<customer_daily[StaticConfig.daily_Custname].length;i++){

                        if(bid.equals(customer_daily[StaticConfig.daily_MilkSr][i])){
                            InnerBgTask4CustUpdateValue task = new InnerBgTask4CustUpdateValue();
                            try {
                                task.execute("setCustomers",CustId, customer_daily[StaticConfig.daily_MilkSr][i],qty1.getText().toString(),
                                        customer_daily[StaticConfig.daily_Rate][i], String.valueOf(Float.valueOf(customer_daily[StaticConfig.daily_Rate][i]) * Float.valueOf(qty1.getText().toString())),
                                        customer_daily[StaticConfig.daily_route][i],customer_daily[StaticConfig.daily_trip][i]).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }

            }
        });

    }

    private void setProducts(final String [][] customer_daily){

        for(int i=0;i<customer_daily[StaticConfig.daily_Custname].length;i++){

            TableRow tr = new TableRow(this);
            tr.setWeightSum(2);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));


            TableRow.LayoutParams trlp;
            trlp =new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT, 1);
            trlp.setMargins(5, 15, 5, 15);
            TextView lbl_prod = new TextView(this);
            lbl_prod.setText(customer_daily[StaticConfig.daily_Category][i]+ "-" +customer_daily[StaticConfig.daily_Sub_Category][i]);
            lbl_prod.setPadding(10, 15, 5, 15);
            lbl_prod.setTextSize(16f);

            lbl_prod.setBackgroundColor(Color.LTGRAY);
            lbl_prod.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            final EditText txt_qty = new EditText(this);

            txt_qty.setSelectAllOnFocus(true);
            txt_qty.setText(customer_daily[StaticConfig.daily_Qty][i]);
            final float rate = Float.valueOf(customer_daily[StaticConfig.daily_Rate][i]);
            final Float currQty = Float.valueOf(txt_qty.getText().toString());
            final float currAmt = Float.valueOf(currQty) * Float.valueOf(rate);

            txt_qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()>=0) {
                        String qty = txt_qty.getText().toString();
                        if(qty.trim().length()<=0){
                            qty="0";
                        }
                        float thisqty = Float .valueOf(qty);

                        if(thisqty<currQty){
                            float change =(currQty-thisqty) *rate;
                            remainamount.setText(String.valueOf(Float.valueOf(remain) - change ));
                            today_lbl.setText(String.valueOf(Float.valueOf(today)-change ));
                            totalamount.setText(String.valueOf(Float.valueOf(total)-change ));

                        }else if (thisqty>currQty){

                            float change =(thisqty-currQty) *rate;
                            remainamount.setText(String.valueOf(Float.valueOf(remain) + change ));
                            today_lbl.setText(String.valueOf(Float.valueOf(today)+change ));
                            totalamount.setText(String.valueOf(Float.valueOf(total)+change ));
                        }else{
                            remainamount.setText(String.valueOf(Float.valueOf(remain) ));
                            today_lbl.setText(String.valueOf(Float.valueOf(today) ));
                            totalamount.setText(String.valueOf(Float.valueOf(total)));
                        }
                        //thisamt=Float.valueOf(qty) * Float.valueOf(rate);
                    }
                }
            });




            txt_qty.setRawInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            txt_qty.setPadding(10, 5, 5, 5);
            txt_qty.setTextSize(16f);

            txt_qty.setBackgroundColor(Color.TRANSPARENT);
            txt_qty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(lbl_prod,trlp );
            tr.addView(txt_qty,trlp );

            TextView hide = new TextView(this);
            hide.setText(customer_daily[StaticConfig.daily_MilkSr][i]);
            hide.setVisibility(View.GONE);
            tr.addView(hide,new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT, 0) );

        }

    }

    class InnerBgTask4CustUpdateValue extends AsyncTask<String,Void,String > {
        Context ctx;
//        private String[][] customer_daily = new String[10][];
        private String customerId="1";
        String url_cust_daily ="http://avinashkumbhar.com/Dairy/updateCustomerDaily.php";
        private boolean ifDataAvailble=false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            //Log.d("kd in daily", "little sure");
            if(!ifDataAvailble) {
                String method = params[0];
                if (method.equals("setCustomers")) {
                    customerId=params[1];
                    Log.d("kd in daily", "sure");
                    return updateCustomers(params);
                }
            }
            return "";
        }
        private String updateCustomers(String... param){

            try {
                return getEmp_info(param);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        private String getEmp_info(String... param) throws JSONException {
            String finalResult = "";

            try {
                URL url = new URL(url_cust_daily);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                Log.d("kd now", "in execut cust  daily");
                OutputStream os = httpURLConnection.getOutputStream();

                String date1=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data= URLEncoder.encode("date","UTF-8")+"="+ URLEncoder.encode(date1,"UTF-8") +"&" +
                        URLEncoder.encode("custid","UTF-8")+"="+ URLEncoder.encode(customerId,"UTF-8") +"&" +
                        URLEncoder.encode("brandid","UTF-8")+"="+ URLEncoder.encode(param[2],"UTF-8") +"&" +
                        URLEncoder.encode("qty","UTF-8")+"="+ URLEncoder.encode(param[3],"UTF-8") +"&" +
                        URLEncoder.encode("rate","UTF-8")+"="+ URLEncoder.encode(param[4],"UTF-8") +"&" +
                        URLEncoder.encode("amount","UTF-8")+"="+ URLEncoder.encode(param[5],"UTF-8") +"&" +
                        URLEncoder.encode("route","UTF-8")+"="+ URLEncoder.encode(param[6],"UTF-8") +"&" +
                        URLEncoder.encode("trip","UTF-8")+"="+ URLEncoder.encode(param[7],"UTF-8")
                        ;

                Log.d("kd.greem","Data Writing-"+ customerId +": "+ data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream IS = httpURLConnection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(IS));
                StringBuilder sb = new StringBuilder("");
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                finalResult = sb.toString();

                IS.close();

//            return finalResult ;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String  result) {
            super.onPostExecute(result);

        }
    }

}

