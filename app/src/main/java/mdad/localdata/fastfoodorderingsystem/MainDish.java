package mdad.localdata.fastfoodorderingsystem;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainDish#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MainDish extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button main_submit_button;
    TextView tv_main1;
    TextView tv_main1price;
    TextView tv_main2;
    TextView tv_main2price;
    EditText main_qty_1;
    EditText main_qty_2;

    String name1, price1,quantity1,name2,price2,quantity2;

    private static final String TAG_ITEM_NAME = "ITEM_NAME";
    private static final String TAG_PRICE = "PRICE";
    private static final String TAG_QUANTITY = "QUANTITY";
    private static String url_update_menu = MainActivity.ipBaseAddress + "/update_menu.php";


    // TODO: Rename and change types and number of parameters
    public static MainDish newInstance() {
        MainDish fragment = new MainDish();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;




    }

    public MainDish() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main_qty_1 = (EditText) getView().findViewById(R.id.main_qty_1);
        main_qty_2 = (EditText) getView().findViewById(R.id.main_qty_2);
        tv_main1 = (TextView) getView().findViewById(R.id.tv_main1);
        tv_main2 = (TextView) getView().findViewById(R.id.tv_main2);
        tv_main1price = (TextView) getView().findViewById(R.id.tv_main1price);
        tv_main2price = (TextView) getView().findViewById(R.id.tv_main2price);
        main_submit_button = (Button)  getView().findViewById(R.id.main_submit_button);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        main_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check_data();
            }
            private void check_data() {
                if (main_qty_1 != null ) {
                    submit_app1();
                    // to be confirm is from here send to Database , if yes create function, function will be take to JSON
                } else if (main_qty_2 != null ) {
                    submit_app2();
                }

            }

            });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_dish, container, false);
    }

    public void submit_app2() {

        name2 = tv_main2.getText().toString();
        price2 = tv_main2price.getText().toString();
        quantity2 = main_qty_2.getText().toString();

        JSONObject dataJson = new JSONObject();

        try {
            dataJson.put(TAG_ITEM_NAME, name2);
            dataJson.put(TAG_PRICE, price2);
            dataJson.put(TAG_QUANTITY, quantity2);

        } catch (JSONException e) {

        }
        postData(url_update_menu, dataJson);
    }

    public void submit_app1() {

        name1 = tv_main1.getText().toString();
        price1 = tv_main1price.getText().toString();
        quantity1 = main_qty_1.getText().toString();

        JSONObject dataJson = new JSONObject();

        try {
            dataJson.put(TAG_ITEM_NAME, name1);
            dataJson.put(TAG_PRICE, price1);
            dataJson.put(TAG_QUANTITY, quantity1);

        } catch (JSONException e) {

            Log.i("JSONException", e.toString());

        }
        postData(url_update_menu, dataJson);
    }



    public void postData(String url, final JSONObject json) {


        //Log.i("request", "Testing");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //Log.i("request", "Testing2");


        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i("aabc", "Success");
                //checkResponse(response, json); //post only
            }

        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(json_obj_req);
    }}


