package mdad.localdata.fastfoodorderingsystem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
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
        //scanCode(); // taken out to QR_Page

        // 24/7to be fix, havent done
        //Intent incomingintent =getIntent();
        //String tableNumget = incomingintent.getStringExtra("tableNum");

        Bundle extras = getIntent().getExtras();
        String tableNum="000";

        if (extras != null) {
            tableNum = extras.getString("tableNum");
            // and get whatever type user account id is
        }
        // log cause crash for some reason, to be fix again, updated fixed

        Log.i("tableNum", tableNum); //when set this will crash, but it show i success to get QR scan , unable show on text view, only show as Null
        tv_Tablenum = (TextView) findViewById(R.id.tv_Tablenum);
        tv_Tablenum.setText("Table number: " + tableNum ); // ffs , for some reason it will reload back even i success, something is wrong,but i duno where

//inflating tab layout
        TabLayout tabLayout =( TabLayout) findViewById(R.id.tab_layout);
//displaying tabs
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(titles[position])).attach();

    }
//    private String tablenumsend = getIntent().getStringExtra("tableNum");
//    public String gettablenum(){
//        return tablenumsend;
//    }


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
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
//comment out , to be remove
/*
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);


    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result ->
    {
        if(result.getContents() !=null)
        {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);

            tv_Tablenum =findViewById(R.id.tv_Tablenum);

            tv_Tablenum.setText("Table "+result.getContents());
            tablenum =Integer.parseInt(result.getContents());
            sendData();

        }
    });


    private void sendData(){


        JSONObject dataJson = new JSONObject();

        try{
            dataJson.put(TAG_TABLE_ID, tablenum);

        }catch(JSONException e){

            Log.i("JSON Exception  =======", e.toString());

        }
        postData(url_send_tablenum,dataJson);

    }
    public void postData(String url, final JSONObject json){

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Log.i("URL is =======", url);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getInt(TAG_SUCCESS)==1){
                        finish();
                         Intent i = new Intent(getApplicationContext(), MainActivity.class);
                         startActivity(i);

                        Log.i("Update successful", "Update ok");

                    }else{


                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error in Volley",Toast.LENGTH_LONG).show();
            }

        });

        requestQueue.add(json_obj_req);
    }
*/



}

