package com.example.calorius;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    //A partir de aquí pasan cosas de HTTP REST

    private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {

        private int idCli;
        private String nombCli;
        private int telefCli;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            //HttpClient httpClient = new DefaultHttpClient();
            String url = "laurl:123/Apli/Usuarios/Usuario......";
            URL objUrl = null;
            try { //me pedía envolverlo en try catch
                objUrl = new URL("la URL");
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) objUrl.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accpet", "application/json");
                urlConnection.setRequestMethod("POST");

                JSONObject dato = new JSONObject();
                dato.put("email", params[0]);
                dato.put("password", params[1]);

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(dato.toString());
                wr.flush();

                int httpResponse = urlConnection.getResponseCode();
                if(httpResponse == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String valorDevuelto = null;
                    valorDevuelto = br.readLine(); //en comentario tutorial ponía br.Line()
                    if(!valorDevuelto.equals("true"))
                    resul = false;
                }
                else
                {
                    Log.e("ServicioRest", "Error resultado" + httpResponse);
                    resul = false;
                }

            } catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            /*try{
                InputStream in =  new BufferedInputStream(urlConnection.getInputStream());
                readStream(in); //este ejemplo tiene un método que lee el stream. No sé si lo
                                //tenemos que hacer así...
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                urlConnection.disconnect();
            }
            String id = params[0];

            HttpGet del =
                    new HttpGet("http://10.107.57.21:51674/Api/Clientes/Cliente/" + id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);

                idCli = respJSON.getInt("Id");
                nombCli = respJSON.getString("Nombre");
                telefCli = respJSON.getInt("Telefono");
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }*/

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
              //  lblResultado.setText("" + idCli + "-" + nombCli + "-" + telefCli);
            }
        }
    }

}
