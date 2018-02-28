package greem.kd.shopper.main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import greem.kd.shopper.Config.StaticConfig;
import greem.kd.shopper.R;

/**
 * Created by kd on 14/12/17.
 */

public class Customer_view extends Fragment {

    private TextView r1,r2,r3,r4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.customer_activity, container, false);

        r1 = rootView.findViewById(R.id.textView5);
        r2 = rootView.findViewById(R.id.textView6);
        r3 = rootView.findViewById(R.id.textView7);
        r4 = rootView.findViewById(R.id.textView8);

        InnerBackgroundTask t1 = new InnerBackgroundTask();
        try {
            t1.execute("getCustomers").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rootView;
    }


    class InnerBackgroundTask extends AsyncTask<String,Void,String> {
        Context ctx;

        String url_cust_info ="http://avinashkumbhar.com/Dairy/accountsummary.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

                String method = params[0];
                if (method.equals("getCustomers")) {
                    return updateCustomers();
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
                URL url = new URL(url_cust_info);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                //String date1=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                //String data= URLEncoder.encode("Date","UTF-8")+"="+URLEncoder.encode(date1,"UTF-8");/*+"&"+*/
                    /*URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");*/

                //bufferedWriter.write(data);
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
            Log.d("kd cust all JSON length", "" + jsonArry);

            for(int i=0;i<jsonArry.length();i++){
                JSONObject c = jsonArry.getJSONObject(i);
                //Log.d("kd json ", "" + c.length());

                setAccountSummaryR1(c.getString("Route1_total"),c.getString("Route1_Paid"),c.getString("Route1_Remain"));
                setAccountSummaryR2(c.getString("Route2_total"),c.getString("Route2_Paid"),c.getString("Route2_Remain"));
                setAccountSummaryR3(c.getString("Route3_total"),c.getString("Route3_Paid"),c.getString("Route3_Remain"));
                setAccountSummaryR4(c.getString("Route4_total"),c.getString("Route4_Paid"),c.getString("Route4_Remain"));
                //Log.d("kd data", "" + sr +" "+ CustName);
            }

            return "";
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public void setAccountSummaryR1(String total,String paid,String remain){
        r1.setText("    Route 1 : \n    " + formatit(total) + " - " + formatit(paid) + "\n      = " + formatit(remain) );
    }
    public void setAccountSummaryR2(String total,String paid,String remain){
        r2.setText("    Route 2 : \n    " + formatit(total) + " - " + formatit(paid) + "\n      = " + formatit(remain) );
    }
    public void setAccountSummaryR3(String total,String paid,String remain){
        r3.setText("    Route 3 : \n     " + formatit(total) + " - " + formatit(paid) + "\n     = " + formatit(remain) );
    }
    public void setAccountSummaryR4(String total,String paid,String remain){
        r4.setText("    Route 4 : \n     " + formatit(total) + " - " + formatit(paid) + "\n     = " + formatit(remain) );
    }

    public String formatit(String txt){

        String abc="";
        NumberFormat formatter = new DecimalFormat("#00,000.00");
        double num = Double.valueOf(txt);
        return formatter.format(num);
    }

}
