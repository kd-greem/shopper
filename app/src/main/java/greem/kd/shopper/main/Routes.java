package greem.kd.shopper.main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import greem.kd.shopper.R;
import greem.kd.shopper.main.route.Route_subView;



/**
 * Created by kd on 14/12/17.
 */

public class Routes extends Fragment {

    private ViewPager viewPager;

    public static Routes getInstance(){
        if(instance==null){
            instance =new Routes();
        }
        return instance;
    }
    private static Routes instance=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.route_activity, container, false);


        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.route_tabs);
        TabLayout.Tab tab_summary = tabLayout.newTab().setText("Route 1");
        TabLayout.Tab tab_routes = tabLayout.newTab().setText("Route 2");
        TabLayout.Tab tab_customer = tabLayout.newTab().setText("Route 3");

        tabLayout.addTab(tab_summary);
        tabLayout.addTab(tab_routes);
        tabLayout.addTab(tab_customer);


        viewPager = (ViewPager) rootView.findViewById(R.id.route_viewpager);
        PagerAdapter adapter = new greem.kd.shopper.main.TabsPagerAdapterRoute(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOffscreenPageLimit(2);

        return rootView;
    }


}

 class TabsPagerAdapterRoute extends FragmentPagerAdapter {
      Route_subView r1,r2,r3;

    public TabsPagerAdapterRoute(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                Log.d("kd tab","adding tab");
                return getRouteOne();
            case 1:
                // Games fragment activity
                return getRouteTwo();

            case 2:
                // Movies fragment activity
                return getRouteThree();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

    public Fragment getRouteOne(){
        if(r1!=null){
            return r1;
        }
        r1 = new Route_subView();
        r1.setRoutenum(1);
        /*InnerBackgroundTask updateuser = new InnerBackgroundTask();
        updateuser.r =r1;
        updateuser.execute("getCustomers");*/
        return r1;
    }
    public Fragment getRouteTwo(){
        if(r2!=null){
            return r2;
        }

        r2 = new Route_subView();
        r2.setRoutenum(2);

        /*InnerBackgroundTask updateuser = new InnerBackgroundTask();
        updateuser.r =r2;
        updateuser.execute("getCustomers");*/
        return r2;
    }
    public Fragment getRouteThree(){
        if(r3!=null){
            return r3;
        }

        r3 = new Route_subView();
        r3.setRoutenum(3);
        /*InnerBackgroundTask updateuser = new InnerBackgroundTask();
        updateuser.execute("getCustomers");*/

        return r3;
    }
/*
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
                 Log.d("Kedar", "in execut + getUser");
                 OutputStream os = httpURLConnection.getOutputStream();

                 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            *//*String data= URLEncoder.encode("auth","UTF-8")+"="+URLEncoder.encode(auth,"UTF-8")+"&"+
                    URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");//+"&"+*//*

            *//*bufferedWriter.write(data);*//*
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
*//*
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //stuff that updates ui
                com.example.zs.voter_tracker.Set_Cast_Surenamewise.getInstant().setUser(contact1,description);
            }
        });*//*


             return customer;
         }
         @Override
         protected void onProgressUpdate(Void... values) {
             super.onProgressUpdate(values);
         }
         @Override
         protected void onPostExecute(String[][] result) {
             super.onPostExecute(result);
             r1.setCustomer(result);
             r1.setSpinnerValues();
             Log.d("kd.greem","in post execute r="+r.getRoutenum());
         }
     }*/
}