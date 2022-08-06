package mdad.localdata.fastfoodorderingsystem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class QR_Page extends AppCompatActivity {
    public static String ipBaseAddress ="http://testmappd.atspace.cc/products";

    String tv_Tablenum;
    String tablenum;
    private static String url_send_tablenum = MainActivity.ipBaseAddress+"/obtain_table_ordersJSON.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_TABLE_ID = "TABLE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_page);
        scanCode();


    }
    private void scanCode(){
        ScanOptions options = new ScanOptions(); //call function for scan
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

            //setting the results obtained from QR scan to a variable
            tablenum=result.getContents();

            //intent it to the MainActivity and send the data of table number
            Intent intent = new Intent(QR_Page.this,MainActivity.class);
            intent.putExtra("tableNum",result.getContents());
            startActivity(intent);



            //This calls the function to send it to database
            sendData();

        }
    });
        //This function will send the table number and request to assign a table ID which is meant to differentiate new guests who are sitting at the same table after a turnover
    private void sendData(){


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
}