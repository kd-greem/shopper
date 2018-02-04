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
        TabLayout.Tab tab_route4 = tabLayout.newTab().setText("Route 4");

        tabLayout.addTab(tab_summary);
        tabLayout.addTab(tab_routes);
        tabLayout.addTab(tab_customer);
        tabLayout.addTab(tab_route4);


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

        viewPager.setOffscreenPageLimit(4);

        return rootView;
    }


}

 class TabsPagerAdapterRoute extends FragmentPagerAdapter {
      Route_subView r1,r2,r3,r4;


     public TabsPagerAdapterRoute(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity

                return getRouteOne();
            case 1:
                // Games fragment activity

                return getRouteTwo();
            case 2:
                // Movies fragment activity
                return getRouteThree();
            case 3:
                return getRouteFour();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
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
    public Fragment getRouteFour(){
         if(r4!=null){
             return r4;
         }

         r4 = new Route_subView();
         r4.setRoutenum(4);
        /*InnerBackgroundTask updateuser = new InnerBackgroundTask();
        updateuser.execute("getCustomers");*/

         return r4;
     }
}