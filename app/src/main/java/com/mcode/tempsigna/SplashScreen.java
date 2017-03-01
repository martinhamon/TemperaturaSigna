package com.mcode.tempsigna;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mcode.temperaturasigna.R;

/**
 * Created by MH on 26/10/2015.
 */
public class SplashScreen extends Activity {
    UsuariosSQLiteHelper usdbh;
    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    private class RegitroGcmcAsyncTask extends AsyncTask<String, String, Object> {

        @Override
        protected void onPreExecute() {

            // mProgressDialog.setMessage("Sincronizando datos con el servidor...");
            // mProgressDialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {
            usdbh = new UsuariosSQLiteHelper(getApplicationContext(), "DBUsuarios", null, 1);
            SQLiteDatabase db = usdbh.getWritableDatabase();

            if (db != null) {


                Cursor c = db.rawQuery("Select user, pass, rmuser, rmpass , mail, centro , id_user  from Usuarios  ", null);

//Nos aseguramos de que existe al menos un registro
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {


                        user_id = c.getInt(5);

                    } while (c.moveToNext());
                }

                //Cerramos la base de datos
                db.close();


            }
            try {

                publishProgress("Sinconizando datos...");
                String registrationToken = Util.ObtenerRegistrationTokenEnGcm(getApplicationContext());

                publishProgress("Sinconizando datos...");
                String respuesta = Util.RegistrarseEnAplicacionServidor(getApplicationContext(), registrationToken, user_id);
                return respuesta;

            } catch (Exception ex) {
                return ex;
            }
        }

        protected void onProgressUpdate(String... progress) {
            // mProgressDialog.setMessage(progress[0]);
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
}
