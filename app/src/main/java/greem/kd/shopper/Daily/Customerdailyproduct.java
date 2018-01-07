package greem.kd.shopper.Daily;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import greem.kd.shopper.Config.StaticConfig;
import greem.kd.shopper.R;

/**
 * Created by kd on 31/12/17.
 */

public class Customerdailyproduct extends AppCompatActivity  {
    private TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.custdailyinfo);
        Bundle extras = getIntent().getExtras();
        String CustId=getIntent().getStringExtra("Custid");

        String[][] customer_daily = (String[][]) extras.getSerializable("custDaily");

        Log.d("kd.greem",""+customer_daily[0].length);
        Log.d("kd.greem",""+customer_daily[0][0].length());

        String route=customer_daily[StaticConfig.daily_route][0];
        String trip=customer_daily[StaticConfig.daily_trip][0];
        String custName =customer_daily[StaticConfig.daily_Custname][0];
        String custphone =customer_daily[StaticConfig.daily_Contact][0];
        ((TextView)findViewById(R.id.txt_route)).setText(route);
        ((TextView)findViewById(R.id.txt_trip)).setText(trip);
        ((TextView)findViewById(R.id.txt_custname_1)).setText(custName);
        ((TextView)findViewById(R.id.txt_custphone)).setText(custphone);

        tl = (TableLayout) findViewById(R.id.table1);

        // Create the table row
        setProducts(customer_daily);

        final TextView totalamount=(TextView) findViewById(R.id.lbl_totalAmount);
        final float totalamt = Float.valueOf(totalamount.getText().toString());
        final TextView remainamount = (TextView) findViewById(R.id.lbl_totalAmount2);
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

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do save page here
                InnerBgTask4CustUpdateValue task = new InnerBgTask4CustUpdateValue();
                task.execute("setCustomers","");
            }
        });

    }

    private void setProducts(String [][] customer_daily){

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

            EditText txt_qty = new EditText(this);
            txt_qty.setText(customer_daily[StaticConfig.daily_Qty][i]);
            txt_qty.setPadding(10, 5, 5, 5);
            txt_qty.setTextSize(16f);
            txt_qty.setBackgroundColor(Color.TRANSPARENT);
            txt_qty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(lbl_prod,trlp );
            tr.addView(txt_qty,trlp );
        }
    }

    class InnerBgTask4CustUpdateValue extends AsyncTask<String,Void,String > {
        Context ctx;
//        private String[][] customer_daily = new String[10][];
        private String customerId="1";
        String url_cust_daily ="http://avinashkumbhar.com/Dairy/getCustomerDailyProdcut1.php";
        private boolean ifDataAvailble=false;
        private final ProgressDialog dialog = new ProgressDialog(getApplicationContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Processing...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //Log.d("kd in daily", "little sure");
            if(!ifDataAvailble) {
                String method = params[0];
                if (method.equals("setCustomers")) {
                    customerId=params[1];
                    Log.d("kd in daily", "sure");
                    return updateCustomers();
                }
            }
            return "";
        }
        private String updateCustomers(){

            try {
                return getEmp_info();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        private String getEmp_info() throws JSONException {
            String finalResult = "";

            try {
                URL url = new URL(url_cust_daily);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                Log.d("kd now", "in execut cust  daily");
                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data= URLEncoder.encode("custid","UTF-8")+"="+ URLEncoder.encode(customerId,"UTF-8") +"&" +
                            URLEncoder.encode("custid","UTF-8")+"="+ URLEncoder.encode(customerId,"UTF-8") +"&" +
                            URLEncoder.encode("custid","UTF-8")+"="+ URLEncoder.encode(customerId,"UTF-8") +"&"
                        ;

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

                Log.d("Customer info in JSON", finalResult);
                IS.close();

//            return finalResult ;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //JSONArray jsonArry = new JSONArray(finalResult);

//            JSONObject jsonObj = new JSONObject(myJSON.substring(myJSON.indexOf("{"), myJSON.lastIndexOf("}") + 1));
//              Log.d("kd json len", "" + jsonArry.length());
//            for(int k=0;k<10;k++)
//                customer_daily[k] = new String[jsonArry.length()];
//
//            for(int i=0;i<jsonArry.length();i++){
//                JSONObject c = jsonArry.getJSONObject(i);
//                //Log.d("kd json ", "" + c.length());
//
//                customer_daily[StaticConfig.daily_Custname][i]= c.getString("Custname");
//                customer_daily[StaticConfig.daily_Category][i] = c.getString("Category");
//                customer_daily[StaticConfig.daily_Sub_Category][i]= c.getString("Sub_Category");
//                customer_daily[StaticConfig.daily_Qty][i]= c.getString("Qty");
//                customer_daily[StaticConfig.daily_Rate][i] = c.getString("Rate");
//                customer_daily[StaticConfig.daily_Amount][i] = c.getString("Amount");
//                customer_daily[StaticConfig.daily_route][i] = c.getString("route");
//                customer_daily[StaticConfig.daily_trip][i] = c.getString("Trip");
//                customer_daily[StaticConfig.daily_Contact][i] = c.getString("Contact");
//                customer_daily[StaticConfig.daily_MilkSr][i] = c.getString("Sr");
                //Log.d("kd data", "" + sr +" "+ CustName);
//            }
/*
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //stuff that updates ui
                com.example.zs.voter_tracker.Set_Cast_Surenamewise.getInstant().setUser(contact1,description);
            }
        });*/

            return "";
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String  result) {
            super.onPostExecute(result);
            dialog.dismiss();
//            setSpinnerValues();
            //addCustDetails(customerId,result);
            //Log.d("kd.greem","in post execute r="+getRoutenum());
        }
    }

}

