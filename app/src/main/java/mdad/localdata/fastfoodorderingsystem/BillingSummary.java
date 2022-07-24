package mdad.localdata.fastfoodorderingsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

//unable to view billing summary printed from database in fragments - no lessons for this
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillingSummary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillingSummary extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ListView lv;
    View root;
    ArrayList<HashMap<String, String>> productsList;
    TextView tvBillTotal;

    // url to get all products list
    private static String url_all_products = MainActivity.ipBaseAddress + "/billing_table_ordersJSON.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLEORDERS = "TABLE_ORDERS";
    private static final String TAG_ITEM_NAME = "ITEM_NAME";
    private static final String TAG_PRICE = "PRICE";
    private static final String TAG_QUANTITY = "QTY";
    private static final String TAG_SUBTOTAL = "SUBTOTAL";
    //private static final String TAG_TOTAL = "TOTAL";
    // products JSONArray
    JSONArray TABLE_ORDERS = null;
    public BillingSummary() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BillingSummary newInstance() {
        BillingSummary fragment = new BillingSummary();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_billing_summary, container, false);
        tvBillTotal = (TextView) root.findViewById(R.id.tvBillTotal);
        lv = root.findViewById(R.id.list);
        // ArrayList for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        // Loading billing items in Background Thread
        postData(url_all_products, null);

        return root;

    }
//postData will obtain the database list of bills and list
    public void postData(String url, final JSONObject json) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                checkResponse(response, json);
            }

        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(json_obj_req);
    }
    private void checkResponse(JSONObject response, JSONObject creds) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                // products found
                // Getting Array of Products
                TABLE_ORDERS = response.getJSONArray(TAG_TABLEORDERS);

                // looping through All Products
                for (int i = 0; i < TABLE_ORDERS.length(); i++) {
                    JSONObject c = TABLE_ORDERS.getJSONObject(i);

                    // Storing each json item in variable
                    String item = c.getString(TAG_ITEM_NAME);
                    String price = c.getString(TAG_PRICE);
                    String quantity = c.getString(TAG_QUANTITY);
                    String subtotal = c.getString(TAG_SUBTOTAL);
                    /*int subtotalint = Integer.parseInt(subtotal);
                    int grandtotal=0;
                    grandtotal+=subtotalint;
                    String.valueOf(grandtotal);
                    tvBillTotal.setText(grandtotal);*/
                    //int total = c.getInt(TAG_TOTAL);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_ITEM_NAME, "Item: "+item+ "       Price: $"+price+ "       Qty: "+quantity);
                    map.put(TAG_SUBTOTAL, "Subtotal: $" +subtotal);
/*                    int grandtotal=0;
                    for (int e=0; e <subtotal.length(); e++){
                        grandtotal += Integer.parseInt(subtotal(e));
                        System.out.println("sum->" + grandtotal);
                    };*/
                    //tvBillTotal.setText(total);


                    // adding HashList to ArrayList
                    productsList.add(map);
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */


                ListAdapter adapter = new SimpleAdapter(
                        getActivity().getApplicationContext(), productsList,
                        R.layout.list_item, new String[]{TAG_ITEM_NAME,
                        TAG_PRICE,TAG_QUANTITY, TAG_SUBTOTAL},
                        new int[]{R.id.item_name, R.id.price, R.id.quantity, R.id.subtotal});
                // updating listview
                lv.setAdapter(adapter);

            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}