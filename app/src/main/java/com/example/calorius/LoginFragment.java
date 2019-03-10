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
        private String fotoUsu = "No se ha provisto";

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            String texto = params[0];

            String url = "http://192.168.0.24:567/Api/Usuarios/Usuario/"+params[0]+"/"; //esto tiene que concretarse

            try { //Comenzamos creando la conexion HTTPURL
                URL objUrl = new URL("http://192.168.0.24:567/Api/Usuarios/Usuario/"+params[0]+"/");
                HttpURLConnection urlConnection = (HttpURLConnection) objUrl.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Accept-Language", "ES");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("GET"); //Operación GET
                urlConnection.connect();
                //Obtenemos códigos de respuesta HTTP para saber si hay errores o no
                int responseCode = urlConnection.getResponseCode();
                String responseMessage = urlConnection.getResponseMessage();

                System.out.println("--> responseCode es: "+ responseCode);
                System.out.println("--> responseMensage es: "+ responseMessage);

                if (responseCode == HttpURLConnection.HTTP_OK){
                    System.out.println("Hurra! Error 200! HTTP OK!");

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //InputStreamReader inReader = new InputStreamReader(in, "UTF-8");

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
                }else{
                    System.out.println("Error HTTP:");
                    System.out.println("--> responseCode es: "+ responseCode);
                    System.out.println("--> responseMensage es: "+ responseMessage);
                }
            } catch(Exception ex)

            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

            protected void onPostExecute(Boolean result) {

                if (result)
                {
                    //lblResultado.setText("-> " + email + " - " + password);
                    //  esto no funciona por alguna razón u otra
                }
            }
        }
}