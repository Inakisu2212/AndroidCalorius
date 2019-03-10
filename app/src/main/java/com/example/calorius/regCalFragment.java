package com.example.calorius;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class regCalFragment extends Fragment {


    public regCalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reg_cal, container, false);
    }

    private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {

        private String email;
        private String password;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            //HttpClient httpClient = new DefaultHttpClient();
            String url = "192.168.0.24:567/Api/Usuarios/Usuario/"; //esto tiene que concretarse
            URL objUrl = null;
            try { //me pedía envolverlo en try catch
                objUrl = new URL(url);
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) objUrl.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("GET");

                JSONObject dato = new JSONObject(); //Construimos el objeto Usuario en formato JSON
                dato.put("email", params[0]);
                dato.put("password", params[1]);

                //Aquí no tengo muy claro qué leches está pasando
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

        protected void onPostExecute(Boolean result) { //A partir de aquí hacer que funcione

            if (result)
            {
                //  lblResultado.setText("" + idCli + "-" + nombCli + "-" + telefCli);
            }
        }
    }
}
