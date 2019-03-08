package com.example.calorius;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button botonLogin;
    private TextView textoEmail, textoPasswd;
    private TextView lblResultado;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        textoEmail = (TextView) v.findViewById(R.id.emailText);
        textoPasswd = (TextView) v.findViewById(R.id.passwdText);

        botonLogin = (Button) v.findViewById(R.id.loginButton);
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSObtener tareaAsincrona = new TareaWSObtener();

                tareaAsincrona.execute(textoEmail.getText().toString(),
                        textoPasswd.getText().toString());
            }
        });
        return v;
    }

    @Override
    public void onClick(View v) {

    }


    //A partir de aquí pasan cosas de HTTP REST

    private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {

        private String email;
        private String password;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            String texto = params[0];
            //HttpClient httpClient = new DefaultHttpClient();
            StringBuilder result = new StringBuilder();
            String url = "http://192.168.0.24:567/Api/Usuarios/Usuario/"+params[0]+"/"; //esto tiene que concretarse
            URL objUrl = null;
            try { //me pedía envolverlo en try catch
                objUrl = new URL(url);
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) objUrl.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true); //puede que esto sobre
                urlConnection.setRequestProperty("Content-Type", "application/json");
                //urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("GET");

                int responseCode = urlConnection.getResponseCode();
                System.out.println("--> responseCode es: "+ responseCode);
                //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(urlConnection.getInputStream()));

                String line;
                StringBuffer response = new StringBuffer();
                while ((line = reader.readLine())!=null){
                    response.append(line);
                }

                JSONObject datoObtenido = new JSONObject(response.toString()); //Construimos el objeto Usuario en formato JSON
                String email = datoObtenido.getString("email");
                String passwd = datoObtenido.getString("password");

            } catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

                //Aquí no tengo muy claro qué leches está pasando
//                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
//                wr.write(dato.toString());
//                wr.flush();
//
//                int httpResponse = urlConnection.getResponseCode();
//                if(httpResponse == HttpURLConnection.HTTP_OK)
//                {
//                    BufferedReader br = new BufferedReader(
//                            new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
//                    String valorDevuelto = null;
//                    valorDevuelto = br.readLine(); //en comentario tutorial ponía br.Line()
//                    if(!valorDevuelto.equals("true"))
//                        resul = false;
//                }
//                else
//                {
//                    Log.e("ServicioRest", "Error resultado" + httpResponse);
//                    resul = false;
//                }
//
//            } catch(Exception ex)
//            {
//                Log.e("ServicioRest", "Error!", ex);
//                resul = false;
//            }


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
              lblResultado.setText("-> " + email + " - " + password);
            }
        }
    }

}
