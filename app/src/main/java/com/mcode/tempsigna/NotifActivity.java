package com.mcode.tempsigna;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mcode.temperaturasigna.R;

import org.json.JSONObject;

public class NotifActivity extends AppCompatActivity {


    private String URL;
    private EditText mensage;
    private Button enviar;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mensage = (EditText) findViewById(R.id.Mensaje);
        enviar = (Button) findViewById(R.id.sendmsg);
        enviar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                mProgressDialog.show();
                sendMsg();

            }
        });

    }

    private void sendMsg() {

        final String URL = "http://www.m-code.com.ar/Old/Android/mensaje_centros.php?mensaje=" + Uri.encode(mensage.getText().toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        mProgressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // TODO Auto-generated method stub
                        mProgressDialog.dismiss();
                    }

                });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);

// add the request object to the queue to be executed
        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }
}
