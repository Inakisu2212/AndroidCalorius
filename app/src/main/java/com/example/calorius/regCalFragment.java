package com.example.calorius;


import android.annotation.TargetApi;
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

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;


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

    @TargetApi(11)
    private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            String codigoAl = params[0];

            //Preparamos la conexión HTTP
            HttpClient httpClient = new DefaultHttpClient();
            String laUrl;
            if (codigoAl != null){//Dependiendo de si pedimos un alimento o todos.
                laUrl = "http://192.168.0.24:567/Api/Alimentos/Alimento/" + codigoAl+"/";
            }else{
                laUrl = "http://192.168.0.24:567/Api/Alimentos";
            }
            HttpGet del = new HttpGet(laUrl);
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

                System.out.println("Devuelve: " + emailUsu + " - " + passwdUsu + " - ");
                resul = true;

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
