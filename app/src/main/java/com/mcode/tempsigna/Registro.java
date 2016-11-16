package com.mcode.tempsigna;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mcode.temperaturasigna.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Registro extends AppCompatActivity {
    private ProgressDialog progress;
    String user;
    String email;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Button registrobtn = (Button) findViewById(R.id.btnRegister);

        TextView txtregistro = (TextView) findViewById(R.id.link_to_login);

        txtregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(Registro.this, LoginActivity.class);
                Registro.this.startActivity(itemintent);
                finish();
            }
        });
        final SendData tarea = new SendData(this);
        registrobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = ((TextView) findViewById(R.id.txtUserName)).getText().toString();
                email = ((TextView) findViewById(R.id.txtEmail)).getText().toString();
                pass = ((TextView) findViewById(R.id.txtPass)).getText().toString();
                tarea.execute();
            }
        });
    }


    private class SendData extends AsyncTask<String, Void, Void> {

        private final Context context;

        public SendData(Context c) {

            this.context = c;
//            this.error = status;
//            this.type = t;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {


                URL url = new URL("http://www.m-code.com.ar/Old/Android/registroandroid.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = "name=" + user + "&email=" + email + "&pass=" + pass;

                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                Registro.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        String salida = output.toString();
                        if (salida.indexOf("SUCCESS") != -1) {
                            progress.dismiss();
                            Intent itemintent = new Intent(Registro.this, LoginActivity.class);
                            Registro.this.startActivity(itemintent);
                            finish();
                        }


                    }
                });


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            progress.dismiss();
        }

    }

}






