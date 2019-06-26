package com.india.shopping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class HomeActivity extends AppCompatActivity  implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private TextView tv;
    private List<String> list;
    private RequestQueue mq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        list = new ArrayList<String>();




    }

    public void scanCode(View view){
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(this);
        setContentView(scannerView);
        scannerView.startCamera();
    }

    @Override
    public void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
            String resultCode = result.getText();
            setContentView(R.layout.activity_home);


            //list.add(resultCode);
            String url = "http://192.168.0.103:8080/cost/getcost/1211";
            mq = Volley.newRequestQueue(this);
            JsonObjectRequest jsor = new JsonObjectRequest( Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                list.add(response.get("name").toString());
                                System.out.println(response);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                }
            });

            mq.add(jsor);

            ListAdapter adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, list);
            ListView listView = (ListView)findViewById(R.id.itemList);
            listView.setAdapter(adapter);




            scannerView.stopCamera();
    }
}
