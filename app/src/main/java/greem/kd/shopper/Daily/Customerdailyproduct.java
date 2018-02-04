package greem.kd.shopper.Daily;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import greem.kd.shopper.Config.StaticConfig;
import greem.kd.shopper.R;
import greem.kd.shopper.main.route.Route_subView;

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
    DecimalFormat df = new DecimalFormat("#.##");
    private EditText paid;
    private Button EditBtn;
    private Button btn_save;
    private float todaystotalamountInit=0;
    public static int datepointer;
    public static String from="";
    float x1,y1;
    float x2,y2;
    String gl_date="";
    private String CustId;
    private String pre;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("kd","back pressed");
        Intent intent=new Intent();
        setResult(2,intent);
        finish();//finishing activity
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("kd greem","in Intent OnActivityResult");
        this.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.custdailyinfo);
        if(from.equals("")) {
            overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
        }else if(from.equals("left")) {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }else if(from.equals("right")) {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
        }
        Bundle extras = getIntent().getExtras();
        CustId=getIntent().getStringExtra("Custid");
        today=getIntent().getStringExtra("today");
        pre=getIntent().getStringExtra("pre");
        total=getIntent().getStringExtra("total");
        remain=getIntent().getStringExtra("remain");
        df.setRoundingMode(RoundingMode.HALF_UP);

        gl_date=getIntent().getStringExtra("Date");

        EditBtn = (Button) findViewById(R.id.btn_edit);

        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableEdit(true);
            }
        });

        final String[][] customer_daily = (String[][]) extras.getSerializable("custDaily");

        Log.d("kd.greem",""+customer_daily[0].length);
        //Log.d("kd.greem",""+customer_daily[0][0].length());

        TextView lblDate = (TextView) findViewById(R.id.lbl_date);
//        String formattedDate = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        lblDate.setText(gl_date);

        String route=customer_daily[StaticConfig.daily_route][0];
        String trip=customer_daily[StaticConfig.daily_trip][0];
        String custName =customer_daily[StaticConfig.daily_Custname][0];
        String custphone =customer_daily[StaticConfig.daily_Contact][0];

        ((TextView)findViewById(R.id.txt_route)).setText(route);
        ((TextView)findViewById(R.id.txt_trip)).setText(trip);
        ((TextView)findViewById(R.id.txt_custname_1)).setText(custName);

        today_lbl=(TextView)findViewById(R.id.txt_today);
        today_lbl.setText(String.valueOf(df.format(Float.valueOf(today))));
        pre_lbl=(TextView)findViewById(R.id.txt_pre);
        pre_lbl.setText(String.valueOf(df.format(Float.valueOf(pre))));

        tl = (TableLayout) findViewById(R.id.table1);

        // Create the table row

        totalamount=(TextView) findViewById(R.id.lbl_totalAmount);
        totalamount.setText(String.valueOf(df.format(Float.valueOf(total))));

        remainamount = (TextView) findViewById(R.id.lbl_remainAmount);
        remainamount.setText(String.valueOf(df.format(Float.valueOf(remain))));


        totalamt =new Float(df.format(Float.valueOf(total)));
        todayamt =new Float(df.format(Float.valueOf(today)));
        remainamt =new Float(df.format(Float.valueOf(remain)));

        paid= (EditText) findViewById(R.id.txt_paidamount);
        paid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float totalhere = new Float(df.format(Float.valueOf(totalamount.getText().toString())));
                if(s.length()>0) {
                    Float subamount= totalhere - Float.valueOf(s.toString());
                    remainamount.setText(String.valueOf(df.format(subamount)));
                }
                if(s.length()==0){
                    remainamount.setText(String.valueOf(df.format(totalhere )));
                }
            }
        });
        setProducts(customer_daily);

        btn_save = (Button) findViewById(R.id.button2);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do save page here
                Toast.makeText(Customerdailyproduct.this,"Saving Changes ! Please wait !",Toast.LENGTH_SHORT).show();

                EnableEdit(false);
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
                            String methodName="setCustomers";
                            if(datepointer>0){
                                methodName="updatenew";
                            }

                            try {
                                task.execute(methodName,CustId, customer_daily[StaticConfig.daily_MilkSr][i],qty1.getText().toString(),
                                        customer_daily[StaticConfig.daily_Rate][i], String.valueOf(new Float(df.format(Float.valueOf(customer_daily[StaticConfig.daily_Rate][i]) * Float.valueOf(qty1.getText().toString())))),
                                        customer_daily[StaticConfig.daily_route][i],customer_daily[StaticConfig.daily_trip][i]).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    //updating account_final table with today pre total and remain;
                    InnerBgTask4CustUpdateValue task = new InnerBgTask4CustUpdateValue();
                    try {
                        task.execute("updateAccount",CustId, today_lbl.getText().toString(),pre_lbl.getText().toString(),
                                totalamount.getText().toString(), paid.getText().toString(),remainamount.getText().toString()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
                Toast.makeText(Customerdailyproduct.this,"Changes Saved !",Toast.LENGTH_SHORT).show();
            }
        });

        //setting all editable to false;
        EnableEdit(false);

    }
    public void addCustDetails(String gl_date, String CustomerID,String [][] customer_daily,String today,String pre,String total,String remain){

        Intent myIntent1 = new Intent(getBaseContext(), Customerdailyproduct.class);
        myIntent1.putExtra("layout", R.layout.custdailyinfo);
        myIntent1.putExtra("Custid", CustomerID);
        myIntent1.putExtra("today", today);
        myIntent1.putExtra("pre", pre);
        myIntent1.putExtra("total", total);
        myIntent1.putExtra("remain", remain);
        myIntent1.putExtra("Date",gl_date);

        Bundle mBundle = new Bundle();

        mBundle.putSerializable("custDaily", customer_daily);
        myIntent1.putExtras(mBundle);
        startActivityForResult(myIntent1,2);

//        LayoutInflater vi = (LayoutInflater) rootView.getContext().getSystemService(rootView.getContext().LAYOUT_INFLATER_SERVICE);
//        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.firstlayout);
//        linear.addView(Custview);
//        rootView.invalidate();
    }

    private void EnableEdit(boolean flag){

        /*paid.setFocusable(flag);
        paid.setClickable(flag);
        btn_save.setEnabled(flag);
        EditBtn.setEnabled(!flag);
        for(int c=1; c<tl.getChildCount();c++){
            TableRow row = (TableRow)tl.getChildAt(c);
            EditText qty1 = (EditText) row.getChildAt(1);
            qty1.setClickable(flag);
            qty1.setFocusable(flag);

        }*/

        paid.setEnabled(flag);
        btn_save.setEnabled(flag);
        EditBtn.setEnabled(!flag);
        for(int c=1; c<tl.getChildCount();c++){
            TableRow row = (TableRow)tl.getChildAt(c);
            EditText qty1 = (EditText) row.getChildAt(1);
            qty1.setEnabled(flag);

        }

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
            final float rate = new Float(df.format(Float.valueOf(customer_daily[StaticConfig.daily_Rate][i])));
            final float currQty = new Float(df.format(Float.valueOf(txt_qty.getText().toString())));
            final float currAmt = new Float(df.format(Float.valueOf(currQty) * Float.valueOf(rate)));
            
            todaystotalamountInit+=currAmt;
            
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

                        float thisqty = Float.valueOf(qty);

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

        if(Float.valueOf(today) ==0) {
            remain=String.valueOf(Float.valueOf(remain) + todaystotalamountInit);
            today =String.valueOf(Float.valueOf(today) + todaystotalamountInit);
            total=String.valueOf(Float.valueOf(total) + todaystotalamountInit);
            remainamount.setText(String.valueOf(Float.valueOf(remain) ));
            today_lbl.setText(String.valueOf(Float.valueOf(today)));
            totalamount.setText(String.valueOf(Float.valueOf(total)));
        }
    }

    class InnerBgTask4CustUpdateValue extends AsyncTask<String,Void,String > {
        Context ctx;
//        private String[][] customer_daily = new String[10][];
        private String customerId="1";
        String url_cust_daily ="http://avinashkumbhar.com/Dairy/updateCustomerDaily.php";
        String url_account_final ="http://avinashkumbhar.com/Dairy/updateaccountfinal.php";
        String url_cust_daily4Newdate = "http://avinashkumbhar.com/Dairy/updateCustomerDaily4new.php";

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
                    return updateCustomers(params);
                }
                else if(method.equals("updateAccount")) {
                    customerId=params[1];
                    return updateAccount(params);
                }else if(method.equals("updatenew")){
                    url_cust_daily=url_cust_daily4Newdate;
                    customerId=params[1];
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
        private String updateAccount(String...param){

            try {
                return updateAccountInfo(param);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        private String updateAccountInfo(String... param) throws JSONException {
            String finalResult = "";

            try {
                URL url = new URL(url_account_final);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();

                String date1=gl_date;

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data= URLEncoder.encode("date","UTF-8")+"="+ URLEncoder.encode(date1,"UTF-8") +"&" +
                        URLEncoder.encode("custid","UTF-8")+"="+ URLEncoder.encode(customerId,"UTF-8") +"&" +

                        URLEncoder.encode("todays","UTF-8")+"="+ URLEncoder.encode(param[2],"UTF-8") +"&" +
                        URLEncoder.encode("pre","UTF-8")+"="+ URLEncoder.encode(param[3],"UTF-8") +"&" +
                        URLEncoder.encode("total","UTF-8")+"="+ URLEncoder.encode(param[4],"UTF-8") +"&" +
                        URLEncoder.encode("paid","UTF-8")+"="+ URLEncoder.encode(param[5],"UTF-8") +"&" +
                        URLEncoder.encode("remain","UTF-8")+"="+ URLEncoder.encode(param[6],"UTF-8")
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

        private String getEmp_info(String... param) throws JSONException {
            String finalResult = "";

            try {
                URL url = new URL(url_cust_daily);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                Log.d("kd now", "in execut cust  daily");
                OutputStream os = httpURLConnection.getOutputStream();

                String date1=gl_date;

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

    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
// when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();
                y2 = touchevent.getY();
//if left to right sweep event on screen
                if (x1 < x2)
                {
                 //   Toast.makeText(this, "--> Left to Right OLD date", Toast.LENGTH_LONG).show();
                    SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
                    datepointer-=1;
                    from="left";
                    //int x = -10;
                    Calendar cal = Calendar.getInstance();
                    cal.add( Calendar.DAY_OF_YEAR, datepointer);
                    Date date1= cal.getTime();

                    Customerdailyproduct.InnerBgTask4CustDaily task = new Customerdailyproduct.InnerBgTask4CustDaily();
                    try {
                        task.execute("getCustomers",CustId,today,pre,total,remain,fromUser.format(date1)).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
// if right to left sweep event on screen
                if (x1 > x2)
                {
                   // Toast.makeText(this, "<-- Right to Left New Date", Toast.LENGTH_LONG).show();
                    datepointer+=1;
                    from="right";
                    SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");

                    Calendar cal = Calendar.getInstance();
                    cal.add( Calendar.DAY_OF_YEAR, datepointer);
                    Date date1= cal.getTime();

                    Customerdailyproduct.InnerBgTask4CustDaily task = new Customerdailyproduct.InnerBgTask4CustDaily();
                    try {
                        task.execute("getCustomers",CustId,today,pre,total,remain,fromUser.format(date1)).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    //Intent i = new Intent(MainActivity.this,Secondpage.class);
                    //startActivity(i);
                }
// if UP to Down sweep event on screen
                if (y1 < y2)
                {
//Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                }
//if Down to UP sweep event on screen
                if (y1 > y2)
                {
// Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }

        class InnerBgTask4CustDaily extends AsyncTask<String,Void,String[][]> {
        Context ctx;
        private String[][] customer_daily = new String[10][];
        private String customerId="1";
        private String today="*0";
        private String pre="*0";
        private String total="*0";
        private String remain="*0";
        private String gl_date;
        String url_cust_daily ="http://avinashkumbhar.com/Dairy/getCustomerDailyProdcut1.php";

        private boolean ifDataAvailble=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[][] doInBackground(String... params) {
            if(!ifDataAvailble) {
                String method = params[0];
                if (method.equals("getCustomers")) {
                    customerId=params[1];
                    today = params[2];
                    pre=params[3];
                    total=params[4];
                    remain =params[5];
                    gl_date = params[6];
                    return updateCustomers();

                }
            }
            return customer_daily;
        }
        private String[][] updateCustomers(){

            try {
                return getEmp_info();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        private String[][] getEmp_info() throws JSONException {
            String finalResult = "";

            try {
                URL url = new URL(url_cust_daily);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();

                Log.d("greem",gl_date);

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data= URLEncoder.encode("custid","UTF-8")+"="+ URLEncoder.encode(customerId,"UTF-8") +"&"+
                URLEncoder.encode("date1","UTF-8")+"="+URLEncoder.encode(gl_date,"UTF-8");

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

            JSONArray jsonArry = new JSONArray(finalResult);

//            JSONObject jsonObj = new JSONObject(myJSON.substring(myJSON.indexOf("{"), myJSON.lastIndexOf("}") + 1));
            //Log.d("kd json len", "" + jsonArry.length());
            for(int k=0;k<10;k++)
                customer_daily[k] = new String[jsonArry.length()];

            for(int i=0;i<jsonArry.length();i++){
                JSONObject c = jsonArry.getJSONObject(i);
                //Log.d("kd.greem", "Customer data Length" + c.length());

                customer_daily[StaticConfig.daily_Custname][i]= c.getString("Custname");
                customer_daily[StaticConfig.daily_Category][i] = c.getString("Category");
                customer_daily[StaticConfig.daily_Sub_Category][i]= c.getString("Sub_Category");
                customer_daily[StaticConfig.daily_Qty][i]= c.getString("Qty");
                customer_daily[StaticConfig.daily_Rate][i] = c.getString("Rate");
                customer_daily[StaticConfig.daily_Amount][i] = c.getString("Amount");
                customer_daily[StaticConfig.daily_route][i] = c.getString("route");
                customer_daily[StaticConfig.daily_trip][i] = c.getString("Trip");
                customer_daily[StaticConfig.daily_Contact][i] = c.getString("Contact");
                customer_daily[StaticConfig.daily_MilkSr][i] = c.getString("Sr");
            }
/*
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //stuff that updates ui
                com.example.zs.voter_tracker.Set_Cast_Surenamewise.getInstant().setUser(contact1,description);
            }
        });*/

            return customer_daily;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String[][] result) {
            super.onPostExecute(result);
            addCustDetails(gl_date,customerId,result,today,pre,total,remain);
        }
    }

}

