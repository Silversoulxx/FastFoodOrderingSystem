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
    Button btn_scan;
    TextView tv_Tablenum;
    int tablenum;
    private static String url_send_tablenum = MainActivity.ipBaseAddress+"/obtain_table_ordersJSON.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_TABLE_ID = "TABLE_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_Tablenum = (TextView) findViewById(R.id.tv_Tablenum);
        pagerAdapter = new MyPagerAdapter(this);
        viewPager = findViewById(R.id.mypager);
        viewPager.setAdapter(pagerAdapter);
        scanCode();

//inflating tab layout
        TabLayout tabLayout =( TabLayout) findViewById(R.id.tab_layout);
//displaying tabs
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(titles[position])).attach();

//sending assigned table number to database
        tablenum = 1;

        JSONObject dataJson = new JSONObject();

        try{
            dataJson.put(TAG_TABLE_ID, tablenum);

        }catch(JSONException e){

        }
        postData(url_send_tablenum,dataJson);



    }

    public void postData(String url, final JSONObject json){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getInt(TAG_SUCCESS)==1){
                        finish();
                        Intent i = new Intent(getApplicationContext(), BillingSummary.class);
                        startActivity(i);

                    }else{

                        // product with pid not found
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error in Volley",Toast.LENGTH_LONG).show();
            }

        });

        requestQueue.add(json_obj_req);
    }




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
// return SecondFragment.newInstance( );
                }
                case 2: {
// return ThirdFragment.newInstance( );
                }
                case 3: {
//return BillingSummary.newInstance( );
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

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Result");
            builder.setMessage("Table "+result.getContents());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();

            tv_Tablenum =findViewById(R.id.tv_Tablenum);

            tv_Tablenum.setText("Table "+result.getContents());

        }
    });
}
