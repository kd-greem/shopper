package greem.kd.shopper.Daily;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    
}

