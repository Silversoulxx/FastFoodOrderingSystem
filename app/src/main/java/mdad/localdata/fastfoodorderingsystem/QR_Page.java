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
        //scanCode();
        // here to//
        Intent intent = new Intent(QR_Page.this,MainActivity.class);
        intent.putExtra("tableNum","1");
        startActivity(intent);
        // here // is to be delete, this is for test only

    }
    private void scanCode(){
        ScanOptions options = new ScanOptions(); //summon function for scan
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
            /*AlertDialog.Builder builder = new AlertDialog.Builder(QR_Page.this);//<< please put QR_Page not main activity
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();*/

            tablenum=result.getContents();

            //24/7 to be fix, updated fixed
            Intent intent = new Intent(QR_Page.this,MainActivity.class);
            intent.putExtra("tableNum",result.getContents());
            startActivity(intent);




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
//                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(i);


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
}