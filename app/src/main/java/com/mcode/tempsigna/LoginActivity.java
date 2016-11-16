package com.mcode.tempsigna;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via idusuario/password.
 */
public class LoginActivity extends Activity {

    private TextView lblGotoRegister;
    private Button btnLogin;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView loginErrorMsg;
    private CheckBox checkuser;
    private CheckBox chekpass;
    private Usuario usuario;
    private String password;
    private String idusuario;
    UsuariosSQLiteHelper usdbh;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.txtEmail);
        inputPassword = (EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        checkuser = (CheckBox) findViewById(R.id.checkBoxuser);
        chekpass = (CheckBox) findViewById(R.id.checkBoxpass);
        TextView txtregistro = (TextView) findViewById(R.id.link_to_register);

        txtregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(LoginActivity.this, Registro.class);
                LoginActivity.this.startActivity(itemintent);
                finish();

            }
        });


        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        String user = "";
        String pass = "";
        String rmuser = "";
        String rmpass = "";
        //Si hemos abierto correctamente la base de datos
        if (db != null) {


            Cursor c = db.rawQuery("Select user, pass, rmuser, rmpass from Usuarios  ", null);

//Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    user = c.getString(0);
                    pass = c.getString(1);
                    rmuser = c.getString(2);
                    rmpass = c.getString(3);

                } while (c.moveToNext());
            }

            //Cerramos la base de datos
            db.close();
            if (rmuser != null && !rmuser.isEmpty() && rmuser.equals("1")) {
                inputEmail.setText(user);
                checkuser.setChecked(true);
            }
            if (rmpass != null && !rmpass.isEmpty() && rmpass.equals("1")) {
                inputPassword.setText(pass);
                chekpass.setChecked(true);
            }

        }


        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                idusuario = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                if (!idusuario.isEmpty() && !password.isEmpty())

                {
                    makeJsonArrayRequest();
                } else {
                    loginErrorMsg.setText(getString(R.string.error_cuenta));
                }


            }
        });


    }

    private void makeJsonArrayRequest() {

        final String URL = "http://www.m-code.com.ar/Old/login.php?usuario=" + idusuario + "&pass=" + password;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    // Parsing json array response
                    // loop through each json object

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject usr = (JSONObject) response.get(i);
                        int li = 0;

                        String nusuario = usr.getString("usuario");
                        int id = Integer.parseInt(usr.getString("id"));
                        String pass = usr.getString("pass");
                        String fecha = usr.getString("fecha");
                        int activo = Integer.parseInt(usr.getString("activo"));

                        if (!nusuario.equals("inexistente") && activo == 1) {

                            SQLiteDatabase db = usdbh.getWritableDatabase();
                            usdbh.createTable(db);

                            //Si hemos abierto correctamente la base de datos
                            if (db != null) {

                                //Generamos los datos

                                //Insertamos los datos en la tabla Usuarios
                                ContentValues values = new ContentValues();
                                values.put("user", idusuario);
                                values.put("pass", password);
                                values.put("mail", "a@q.com");
                                if (checkuser.isChecked())
                                    values.put("rmuser", "1");
                                else
                                    values.put("rmuser", "0");
                                if (chekpass.isChecked())
                                    values.put("rmpass", "1");
                                else
                                    values.put("rmpass", "0");


                                db.insert("Usuarios", null, values);


                                //Cerramos la base de datos
                                db.close();
                            }
                            Intent itemintent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(itemintent);
                            finish();

                        } else {
                            loginErrorMsg.setText(getString(R.string.error_cuenta));
                        }


                    }


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

    //*******************************
}