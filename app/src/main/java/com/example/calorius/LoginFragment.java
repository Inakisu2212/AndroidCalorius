package com.example.calorius;


import android.annotation.TargetApi;
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

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;


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
            @TargetApi(11)
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
    @TargetApi(11)
    private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {

        private String fotoUsu = "No se ha provisto";

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            String emailIntrod = params[0];
            String passwdIntrod = params[1];

            //Preparamos la conexión HTTP
            HttpClient httpClient = new DefaultHttpClient();

            System.out.println("Email escrito: "+ emailIntrod);
            System.out.println("Passwd escrito: "+ passwdIntrod);
            HttpGet del =
                    new HttpGet("http://10.111.66.10:567/Api/Usuarios/Usuario/" + emailIntrod+"/");
            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                //Creamos el objeto JSON
                JSONObject respJSON = new JSONObject(respStr);
                //Obtenemos valores del objeto JSON para su uso
                String emailUsu = respJSON.getString("email");
                String passwdUsu = respJSON.getString("password");
                fotoUsu = respJSON.getString("foto");

                System.out.println("Devuelve: " + emailUsu + " - " + passwdUsu + " - " + fotoUsu);
                resul = true;
                //lblResultado.setText("" + emailUsu + " - " + passwdUsu + " - " + fotoUsu);
                //------Comprobamos que email y password coincidan
                if(emailUsu.equals(emailIntrod) && passwdUsu.equals(passwdIntrod)){
                    System.out.println("-----> Login correcto!");
                    //No sé si el resto de cosas suceden aquí o en otro sitio
                }


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {

            }
        }
    }

}
