package greem.kd.shopper.Network;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import greem.kd.shopper.Config.StaticConfig;

public class BackgroundTask extends AsyncTask<String,Void,String[][]> {
    Context ctx;

    private String[][] customer= new String[6][];

    String url_cust_info ="http://avinashkumbhar.com/Dairy/getcustinfo.php";


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
        String method=params[0];
        if(method.equals("getCustomers")){
            return updateCustomers();
        }

        return null;
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
            Log.d("Kedar", "in execut + getUser");
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
    }
}
