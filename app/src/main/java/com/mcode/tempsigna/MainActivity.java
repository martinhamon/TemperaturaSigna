package com.mcode.tempsigna;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mcode.temperaturasigna.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Util util;

    Context context;
    configuracion cfg;
    private static String url = "http://www.m-code.com.ar/Old/getdata.php";
    ProgressDialog mProgressDialog;
    FloatingActionButton fab;
    private static String TAG = MainActivity.class.getSimpleName();
    AdapterCentros adapter;
    ArrayList<Centro> arraycentros = new ArrayList<Centro>();
    Random r;
    UsuariosSQLiteHelper usdbh;
    int ran = 0;
    // temporary string to show the parsed response

    // data data
    JSONObject data = null;
    JSONArray tinf = null;


    public MainActivity() {
        context = this;
        adapter = new AdapterCentros(this, arraycentros);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setVisibility(View.GONE);
        r = new Random();
        util = new Util();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setupToolbar();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        RegitroGcmcAsyncTask tarea = new RegitroGcmcAsyncTask();


        if (Util.CheckPlayServices(this)) {

            tarea.execute();
            makeJsonArrayRequest();

        }
        ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(context, ActivityInfo.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                Snackbar.make(view, "Registre su mail", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent;
                intent = new Intent(context, EmailActivity.class);
                startActivity(intent);
            }
        });


    }


    private class RegitroGcmcAsyncTask extends AsyncTask<String, String, Object> {

        @Override
        protected void onPreExecute() {

            mProgressDialog.setMessage("Sincronizando datos con el servidor...");
            mProgressDialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {

            try {

                publishProgress("Sinconizando datos...");
                String registrationToken = Util.ObtenerRegistrationTokenEnGcm(getApplicationContext());

                publishProgress("Sinconizando datos...");
                String respuesta = Util.RegistrarseEnAplicacionServidor(getApplicationContext(), registrationToken);
                return respuesta;

            } catch (Exception ex) {
                return ex;
            }
        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setMessage(progress[0]);
        }

        @Override
        protected void onPostExecute(Object result) {
            // mProgressDialog.dismiss();

            if (result instanceof String) {
                String resulatado = (String) result;
                //Util.MostrarAlertDialog(context, "Registro exitoso. " + resulatado, "GCM", R.mipmap.ic_launcher).show();
            } else if (result instanceof Exception)//Si el resultado es una Excepcion..hay error
            {
                Exception ex = (Exception) result;
                //Util.MostrarAlertDialog(context, ex.getMessage(), "ERROR", R.mipmap.ic_launcher).show();
            }
        }

    }


    //****************************************************
    private void makeJsonArrayRequest() {
        ran = r.nextInt();
        final String URL = "http://www.m-code.com.ar/Old/getdata.php?r=" + ran;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ListView lista = (ListView) findViewById(R.id.listView);
                try {
                    // Parsing json array response
                    // loop through each json object

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject centro = (JSONObject) response.get(i);
                        int li = 0;

                        String nombre_centro = centro.getString("nombre_centro");
                        String temperatura_s1 = centro.getString("temperatura_s1");
                        String temperatura_s2 = centro.getString("temperatura_s2");
                        String humedad = centro.getString("humedad");
                        String fecha = centro.getString("fecha");
                        int control = centro.getInt("control");

                        arraycentros.add(new Centro(nombre_centro, "Temp S1: " + temperatura_s1 + " Hum: " + humedad, "Temp Agua: " + temperatura_s2, humedad, "Ultima lectura: " + fecha, control));

                    }
                    lista.setAdapter(adapter);
                    mProgressDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("CUSTOM_HEADER", "Yahoo");
                headers.put("ANOTHER_CUSTOM_HEADER", "Google");
                return headers;
            }


        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);

// add the request object to the queue to be executed
        AppController.getInstance().addToRequestQueue(req);
    }

    //***************************************************


    private void makeJsonObjectRequest() {
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.icon);
        //b.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_find_location:  // it is going to refer the search id name in main.xml

                //add your code here
                mProgressDialog.show();
                makeJsonArrayRequest();
                arraycentros.clear();
                return true;
            case R.id.about:  // it is going to refer the search id name in main.xml

                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

    }


}


