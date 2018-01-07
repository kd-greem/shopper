package greem.kd.shopper.main.route;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import greem.kd.shopper.Config.StaticConfig;
import greem.kd.shopper.Daily.Customerdailyproduct;
import greem.kd.shopper.R;

/**
 * Created by kd on 17/12/17.
 */

public class Route_subView extends Fragment {

    private Spinner s;
    private HashMap<String,List<View>> CustlistofViews = new HashMap<>();
    public void setCustomer(String[][] customer) {
        Route_subView.customer = customer;
    }
    private List<View> trip1,trip2,trip3,trip4,trip5,trip6,trip7;

    private static String[][] customer=null;
    private String sr;
    private String CustName;
    private String Contact;
    private String Route;
    private String Priority;
    private String Trip;


    public void setRoutenum(int routenum) {
        switch (routenum) {
            case 1:
                this.routenum = "Route - 1";
                break;
            case 2:
                this.routenum = "Route - 2";
                break;
            case 3:
                this.routenum = "Route - 3";
                break;
        }

    }


    public String getRoutenum() {

        return routenum;
    }

    private String routenum ="";
    private List<String> arraySpinner = new ArrayList<>();
    private ArrayAdapter<String> adapter=null;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sub_route_layout, container, false);
        this.arraySpinner = new ArrayList<>();
/*
        this.arraySpinner.add("a");
        this.arraySpinner.add("b");
        this.arraySpinner.add("c");
        this.arraySpinner.add("d");
*/
        s = (Spinner) rootView.findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("kd.Spinneronchange","spinner is " +s.getSelectedItem().toString());
                ResetCustView(s.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        InnerBackgroundTask updateuser = new InnerBackgroundTask();
        updateuser.execute("getCustomers");

        return rootView;

    }


    public void setSpinnerValues(){
        if(!routenum.equals("")){
            while(customer==null){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            List<String> listTrip = new ArrayList<>();
            String[] c=customer[StaticConfig.Route];
            for(int i=0;i<c.length;i++){

                if(c[i].equals(routenum)){
                    listTrip.add(customer[StaticConfig.Trip][i]);
                }
            }
            arraySpinner.clear();
            arraySpinner.add("All Trips");
            List<String> justlist= new ArrayList<>(new HashSet<>(listTrip));
            java.util.Collections.sort(justlist);
            for(String in:justlist){
                arraySpinner.add(in);
            }

            Log.d("kd.in spinner",""+arraySpinner.toString());
            adapter.notifyDataSetChanged();

        }
    }


    public void setSpinnervaluetest(){

        arraySpinner.clear();
        arraySpinner.add("x");
        arraySpinner.add("y");
        arraySpinner.add("z");

        adapter.notifyDataSetChanged();
    }

    public void setCustomerinfo(){

        List<String> listTrip = new ArrayList<>();
        String[] c=customer[StaticConfig.Route];
        for(int i=0;i<c.length;i++){
            if(c[i].equals(routenum)){
                addCust(customer[StaticConfig.CustName][i],"1000","0",customer[StaticConfig.Trip][i],customer[StaticConfig.Sr][i]);
            }
        }
        CustlistofViews.put("Trip - 1",trip1);
        CustlistofViews.put("Trip - 2",trip2);
        CustlistofViews.put("Trip - 3",trip3);
        CustlistofViews.put("Trip - 4",trip4);
        CustlistofViews.put("Trip - 5",trip5);
        CustlistofViews.put("Trip - 6",trip6);
        CustlistofViews.put("Trip - 7",trip7);

    }

    public void addCust(String custName,String Balance,String paid,String trip,final String custid){

        LayoutInflater vi = (LayoutInflater) rootView.getContext().getSystemService(rootView.getContext().LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.route_customer_info, null);

        TextView txtcustname = view.findViewById(R.id.txt_customerName);
        TextView txtbalance = view.findViewById(R.id.txt_balance);
        TextView txtpaid = view.findViewById(R.id.txt_Paid);

        txtcustname.setText(" " + custName);
        txtbalance.setText(" RS: " + Balance);
        txtpaid.setText(" RS: " + paid);

        Log.d("kd.greem", custName+Balance+paid);

        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.firstlayout);

        ((LinearLayout) view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InnerBgTask4CustDaily task = new InnerBgTask4CustDaily();
                try {
                    task.execute("getCustomers",custid).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        linear.addView(view);

        switch (trip){
            case "Trip - 1":
                if(trip1==null){
                    trip1 =new ArrayList<View>();
                }
                trip1.add(view);

                break;
            case "Trip - 2":
                if(trip2==null){
                    trip2 =new ArrayList<View>();
                }
                trip2.add(view);
                break;
            case "Trip - 3":
                if(trip3==null){
                    trip3 =new ArrayList<View>();
                }
                trip3.add(view);
                break;
            case "Trip - 4":
                if(trip4==null){
                    trip4 =new ArrayList<View>();
                }
                trip4.add(view);
                break;
            case "Trip - 5":
                if(trip5==null){
                    trip5 =new ArrayList<View>();
                }
                trip5.add(view);
                break;
            case "Trip - 6":
                if(trip6==null){
                    trip6 =new ArrayList<View>();
                }
                trip6.add(view);
                break;
            case "Trip - 7":
                if(trip7==null){
                    trip7 =new ArrayList<View>();
                }
                trip7.add(view);
                break;
        }

        rootView.invalidate();

    }

    public void addCust(View Custview){

        LayoutInflater vi = (LayoutInflater) rootView.getContext().getSystemService(rootView.getContext().LAYOUT_INFLATER_SERVICE);

        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.firstlayout);

        linear.addView(Custview);

        rootView.invalidate();

    }

    public void ResetCustView(String tripName){

        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.firstlayout);
        if(((LinearLayout) linear).getChildCount() > 0) {
            Log.d("kd.greem","liner view has childs");
            ((LinearLayout) linear).removeAllViews();

        }else{
            Log.d("kd.greem","liner view No Child");
        }

        for(String c:CustlistofViews.keySet()){
            List<View> cust;
            cust = CustlistofViews.get(c);
            if(cust!=null) {
                if (tripName.equals("All Trips")) {
                    for (View v : cust) {
                        addCust(v);
                    }
                } else if (tripName.equals(c)) {
                    for (View v : cust) {
                        addCust(v);
                    }
                }
            }
        }
    }

    public void addCustDetails(String [][] customer_daily){

        Intent myIntent1 = new Intent(rootView.getContext(), Customerdailyproduct.class);
        myIntent1.putExtra("layout", R.layout.custdailyinfo);
        Bundle mBundle = new Bundle();

        mBundle.putSerializable("custDaily", customer_daily);
        myIntent1.putExtras(mBundle);
        startActivity(myIntent1);

//        LayoutInflater vi = (LayoutInflater) rootView.getContext().getSystemService(rootView.getContext().LAYOUT_INFLATER_SERVICE);
//        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.firstlayout);
//        linear.addView(Custview);
//        rootView.invalidate();
    }


    class InnerBackgroundTask extends AsyncTask<String,Void,String[][]> {
        Context ctx;
        private String[][] customer= new String[6][];

        String url_cust_info ="http://avinashkumbhar.com/Dairy/getcustinfo.php";
        private boolean ifDataAvailble=false;
        private String sr;
        private String CustName;
        private String Contact;
        private String Route;
        private String Priority;
        private String Trip;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[][] doInBackground(String... params) {
            if(!ifDataAvailble) {
                String method = params[0];
                if (method.equals("getCustomers")) {
                    return updateCustomers();
                }
            }
            return customer;
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
                URL url = new URL(url_cust_info);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                Log.d("kd now", "in execut + getUser");
                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            /*String data= URLEncoder.encode("auth","UTF-8")+"="+URLEncoder.encode(auth,"UTF-8")+"&"+
                    URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");//+"&"+*/

            /*bufferedWriter.write(data);*/
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

                //  Log.d("Customer info in JSON", finalResult);
                IS.close();

//            return finalResult ;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONArray jsonArry = new JSONArray(finalResult);

//            JSONObject jsonObj = new JSONObject(myJSON.substring(myJSON.indexOf("{"), myJSON.lastIndexOf("}") + 1));
            Log.d("kd all JSON length", "" + jsonArry.length());
            for(int k=0;k<6;k++)
                customer[k] = new String[jsonArry.length()];

            for(int i=0;i<jsonArry.length();i++){
                JSONObject c = jsonArry.getJSONObject(i);
                //Log.d("kd json ", "" + c.length());

                customer[StaticConfig.Sr][i] = c.getString("Sr");
                customer[StaticConfig.CustName][i]= c.getString("Custname");
                customer[StaticConfig.Contact][i] = c.getString("Contact");
                customer[StaticConfig.Route][i]= c.getString("Route");
                customer[StaticConfig.Priority][i]= c.getString("Priority");
                customer[StaticConfig.Trip][i] = c.getString("Trip");
                //Log.d("kd data", "" + sr +" "+ CustName);
            }
/*
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //stuff that updates ui
                com.example.zs.voter_tracker.Set_Cast_Surenamewise.getInstant().setUser(contact1,description);
            }
        });*/

            return customer;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String[][] result) {
            super.onPostExecute(result);
           setCustomer(result);
           setSpinnerValues();
            setCustomerinfo();
           Log.d("kd.greem","in post execute r="+getRoutenum());
        }
    }

    class InnerBgTask4CustDaily extends AsyncTask<String,Void,String[][]> {
        Context ctx;
        private String[][] customer_daily = new String[9][];
        private String customerId="1";
        String url_cust_daily ="http://avinashkumbhar.com/Dairy/getCustomerDailyProdcut1.php";
        private boolean ifDataAvailble=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[][] doInBackground(String... params) {
            Log.d("kd in daily", "little sure");
            if(!ifDataAvailble) {
                String method = params[0];
                if (method.equals("getCustomers")) {
                    customerId=params[1];
                    Log.d("kd in daily", "sure");
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
                Log.d("kd now", "in execut cust  daily");
                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data= URLEncoder.encode("custid","UTF-8")+"="+ URLEncoder.encode(customerId,"UTF-8");

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
            Log.d("kd json len", "" + jsonArry.length());
            for(int k=0;k<9;k++)
              customer_daily[k] = new String[jsonArry.length()];

            for(int i=0;i<jsonArry.length();i++){
                JSONObject c = jsonArry.getJSONObject(i);
                //Log.d("kd json ", "" + c.length());

                customer_daily[StaticConfig.daily_Custname][i]= c.getString("Custname");
                customer_daily[StaticConfig.daily_Category][i] = c.getString("Category");
                customer_daily[StaticConfig.daily_Sub_Category][i]= c.getString("Sub_Category");
                customer_daily[StaticConfig.daily_Qty][i]= c.getString("Qty");
                customer_daily[StaticConfig.daily_Rate][i] = c.getString("Rate");
                customer_daily[StaticConfig.daily_Amount][i] = c.getString("Amount");
                customer_daily[StaticConfig.daily_route][i] = c.getString("route");
                customer_daily[StaticConfig.daily_trip][i] = c.getString("Trip");
                customer_daily[StaticConfig.daily_Contact][i] = c.getString("Contact");
                //Log.d("kd data", "" + sr +" "+ CustName);
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
//            setSpinnerValues();
            addCustDetails(result);
            Log.d("kd.greem","in post execute r="+getRoutenum());
        }
    }
}

