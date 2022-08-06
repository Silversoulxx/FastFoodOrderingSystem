package mdad.localdata.fastfoodorderingsystem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static String ipBaseAddress ="http://testmappd.atspace.cc/products";
    private static final int NUM_PAGES = 4; //4 tabbed views
    //The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.
    public static ViewPager2 viewPager;
    // The pager adapter, which provides the pages to the view pager widget.
    private FragmentStateAdapter pagerAdapter;
    // Array of strings FOR TABS TITLES
    private String[] titles = new String[]{"Appetizer", "Main", "Dessert", "Bill"};
    //Button btn_scan;
    TextView tv_Tablenum;

    private static String url_send_tablenum = MainActivity.ipBaseAddress+"/obtain_table_ordersJSON.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_TABLE_ID = "TABLE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagerAdapter = new MyPagerAdapter(this);
        viewPager = findViewById(R.id.mypager);
        viewPager.setAdapter(pagerAdapter);

        Bundle extras = getIntent().getExtras();
        String tableNum="000";

        if (extras != null) {
            tableNum = extras.getString("tableNum");
            // to get table number scanned
        }
        // to show the table number on the top activity page
        tv_Tablenum = (TextView) findViewById(R.id.tv_Tablenum);
        tv_Tablenum.setText("Table number: " + tableNum ); // ffs , for some reason it will reload back even i success, something is wrong,but i duno where

//inflating tab layout
        TabLayout tabLayout =( TabLayout) findViewById(R.id.tab_layout);
//displaying tabs
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(titles[position])).attach();

    }


//Using PagerAdapter to call 3 fragments and display
    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }
        @Override
        public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
return Appetizer.newInstance( );
                }
                case 1: {
return MainDish.newInstance( );
                }
                case 2: {
return Dessert.newInstance( );
                }
                case 3: {

return BillingSummary.newInstance( );
                }
                default:
                    return new Fragment(); // return a dummy empty fragment first
            }
        }
        @Override
        //This will set the number of tabs
        public int getItemCount() {
            return NUM_PAGES;
        }
    }




}

