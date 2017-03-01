package com.mcode.tempsigna;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by MH on 20/10/2015.
 */
public class MiInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";
    UsuariosSQLiteHelper usdbh;
    int user_id;
    /**
     * Se llama cuando Gcm servers actualizan el registration token, principalemnte por motivos  de seguridad
     */
    @Override
    public void onTokenRefresh() {
        //obtener nuevamente el token y enviarlo a la aplicacion servidor
        RegitroGcmcAsyncTask regitroGcmcAsyncTask = new RegitroGcmcAsyncTask();
        regitroGcmcAsyncTask.execute();
    }

    private class RegitroGcmcAsyncTask extends AsyncTask<String, String, Object> {

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
            Toast.makeText(getApplicationContext(), progress[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof String) {
                String resulatado = (String) result;
                Toast.makeText(getApplicationContext(), "Registro exitoso. " + resulatado, Toast.LENGTH_SHORT).show();
            } else if (result instanceof Exception)//Si el resultado es una Excepcion..hay error
            {
                Exception ex = (Exception) result;
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

}