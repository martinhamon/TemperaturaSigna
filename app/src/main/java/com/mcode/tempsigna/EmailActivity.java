package com.mcode.tempsigna;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mcode.temperaturasigna.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailActivity extends AppCompatActivity {
    ProgressDialog mProgressDialog;
    private static String url = "http://www.m-code.com.ar/Old/Android/emailupdate.php?email=";
    TextView emailtxt;
    Context context;
    UsuariosSQLiteHelper usdbh;
    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btn = (Button) findViewById(R.id.button);
        emailtxt = (TextView) findViewById(R.id.emailtxt);
        context = this;
        mProgressDialog = new ProgressDialog(context);
        usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1);
        SQLiteDatabase db = usdbh.getWritableDatabase();

        if (db != null) {


            Cursor c = db.rawQuery("Select user, pass, rmuser, rmpass , mail from Usuarios  ", null);

//Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    TextView email_act = (TextView) findViewById(R.id.Email_actual);
                    email_act.setText(c.getString(4));
                    user = c.getString(0);

                } while (c.moveToNext());
            }

            //Cerramos la base de datos
            db.close();


        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailtxt.getText().length() > 0 && android.util.Patterns.EMAIL_ADDRESS.matcher(emailtxt.getText().toString()).matches()) {
                    url = url.concat(emailtxt.getText().toString());

                    mProgressDialog.setMessage("Actualizando correo . . .");
                    mProgressDialog.show();
                    sendEmail();
                } else {
                    Util.MostrarAlertDialog(context, "Verifique la direccion de mail!!!", "ERROR", R.drawable.icon).show();
                }

            }
        });


    }


    private void sendEmail() {
        url = url.concat("&user=" + user);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    // Parsing json object response
                    // response will be a json object
                    String name = response.getString("RegistroGcmResult");

                    mProgressDialog.dismiss();
                    Util.MostrarAlertDialog(context, "Direccion actualizada", "Aviso", R.drawable.icon).show();
                    url = "http://www.m-code.com.ar/Old/Android/emailupdate.php?email=";
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                mProgressDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


}
