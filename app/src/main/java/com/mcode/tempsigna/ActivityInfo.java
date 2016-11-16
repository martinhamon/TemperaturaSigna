package com.mcode.tempsigna;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.mcode.temperaturasigna.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ActivityInfo extends AppCompatActivity {
    private XYPlot mySimpleXYPlot;

    LineAndPointFormatter series1Format;
    Number[] series1Numbers;
    XYSeries series1;
    private static String url = "http://m-code.com.ar/Old/getdatahistory.php?id=0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);


        //Range
        mySimpleXYPlot.getGraphWidget().setRangeOriginLinePaint(null);
        mySimpleXYPlot.setRangeBoundaries(16, 29, BoundaryMode.FIXED);

        mySimpleXYPlot.setRangeValueFormat(new DecimalFormat("0"));

        // Creamos dos arrays de prueba. En el caso real debemos reemplazar
        // estos datos por los que realmente queremos mostrar
        makeJsonArrayRequest();

        //Number[] series2Numbers = {4, 6, 3, 8, 2, 10};

        // Añadimos Línea Número UNO:


        // Repetimos para la segunda serie
        // XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers
        //), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");

        // Modificamos los colores de la primera serie
        series1Format = new LineAndPointFormatter(
                Color.rgb(0, 200, 0),                   // Color de la línea
                Color.rgb(0, 100, 0),                   // Color del punto
                Color.rgb(150, 190, 150), null);              // Relleno

        // Una vez definida la serie (datos y estilo), la añadimos al panel
        // mySimpleXYPlot.addSeries(series1, series1Format);

        // Repetimos para la segunda serie
        //  mySimpleXYPlot.addSeries(series2, new LineAndPointFormatter (Color.rgb(0, 0, 200), Color.rgb(0, 0, 100), Color.rgb(150, 150, 190),null));

    }


    private void makeJsonArrayRequest() {
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                try {
                    // Parsing json array response
                    // loop through each json object

                    ArrayList<Double> myV = new ArrayList<Double>();
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject centro = (JSONObject) response.get(i);


                        String nombre_centro = centro.getString("id");
                        String temperatura_s1 = centro.getString("temperatura");

                        String humedad = centro.getString("humedad");

                        myV.add(Double.parseDouble(temperatura_s1));

                    }
                    series1Numbers = new Number[myV.size() + 2];

                    myV.toArray(series1Numbers);

                    series1 = new SimpleXYSeries(
                            Arrays.asList(series1Numbers),  // Array de datos
                            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Sólo valores verticales
                            "Temperaturas"); // Nombre de la primera serie

                    mySimpleXYPlot.addSeries(series1, series1Format);
                    mySimpleXYPlot.redraw();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    //mProgressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // mProgressDialog.dismiss();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }

}
