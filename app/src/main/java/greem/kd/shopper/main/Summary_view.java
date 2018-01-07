package greem.kd.shopper.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import greem.kd.shopper.R;

/**
 * Created by kd on 14/12/17.
 */

public class Summary_view extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.summary_view_activity, container, false);

        return rootView;
    }
}
