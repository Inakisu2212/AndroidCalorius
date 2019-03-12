package com.example.calorius;


import android.annotation.TargetApi;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;


/**
 * A simple {@link Fragment} subclass.
 */
@TargetApi(Build.VERSION_CODES.N)
public class regCalFragment extends Fragment {

    private Spinner dropdownAl;
    private CalendarView calendar;
    private String fechaSeleccionada;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public regCalFragment() {
        // Required empty public constructor
    }


    @Override
    @TargetApi(android.os.Build.VERSION_CODES.N)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reg_cal, container, false);
        //Obtenemos el spinner y calendario desde el xml
        dropdownAl =(Spinner)v.findViewById(R.id.spinnerAlimentos);
        calendar = (CalendarView) v.findViewById(R.id.calendarView);
        //Creamos una lista para los alimentos del spinner
        JSONArray jsonAl = obtenerAlimentos();
        String[]  spinnerAlAr = null;
        String[] spinnerNombreAlimentosArray = new String[jsonAl.length()];//Array con nombres alim.
        final String[] spinnerAlimentosArray = new String[jsonAl.length()];//Array con objs. alim.
        for(int i = 0; i<jsonAl.length();i++){
            try {
                JSONObject jAl = jsonAl.getJSONObject(i);
                spinnerAlimentosArray[i] = jAl.toString();//Lista para obtener obj. alim. al seleccionar del spinner
                String nombre = jAl.getString("nombre");
                spinnerNombreAlimentosArray[i]=nombre; //Introd. nombres alim. en spinner
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinnerAlAr = spinnerNombreAlimentosArray;
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerAlAr);
        //set the spinners adapter to the previously created one.
        dropdownAl.setAdapter(adapter);
        //Ejecutamos para introducir valores en la base de datos
        Button botonReg = (Button) v.findViewById(R.id.botonReg);
        //Obtenemos la fecha introducida en el calendarView
        botonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.N)
            public void onClick(View v) {

                //Obtener el id del alimento que se ha seleccionado
                int idAlSeleccionado = dropdownAl.getSelectedItemPosition();
                String alSeleccionado = spinnerAlimentosArray[idAlSeleccionado];
                fechaSeleccionada = sdf.format(new Date(calendar.getDate()));
                regCalFragment.TareaWSObtener tareaAsincrona = new regCalFragment.TareaWSObtener();
                System.out.println("Fecha: "+fechaSeleccionada+" jsonAlimento: "+alSeleccionado);
                tareaAsincrona.execute(alSeleccionado, fechaSeleccionada);
            }
        });
        return v;
    }

    public JSONArray obtenerAlimentos() { //Conexión para obtener alimentos
        JSONArray jsonArray = new JSONArray();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

            //Preparamos la conexión HTTP
            HttpClient httpClient = new DefaultHttpClient();
            String laUrl;
            laUrl = "http://10.111.66.10:567/Api/Alimentos";

            HttpGet del = new HttpGet(laUrl);
            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray jsonAl = new JSONArray(respStr);
                for (int j = 0 ; j<jsonAl.length() ; j++){
                    JSONObject jOb = jsonAl.getJSONObject(j);
                    jsonArray.put(j, jOb);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return jsonArray;

    }
        @TargetApi(11)
        private class TareaWSObtener extends AsyncTask<String, Integer, Boolean> {

            protected Boolean doInBackground(String... params) {

                boolean resul = true;
                String codigoAl = params[0];

                //Preparamos la conexión HTTP
                HttpClient httpClient = new DefaultHttpClient();
                String laUrl;
                if (codigoAl != null) {//Dependiendo de si pedimos un alimento o todos.
                    laUrl = "http://192.168.0.24:567/Api/Alimentos/Alimento/" + codigoAl + "/";
                } else {
                    laUrl = "http://192.168.0.24:567/Api/Alimentos";
                }
                HttpGet del = new HttpGet(laUrl);
                del.setHeader("content-type", "application/json");

                try {
                    HttpResponse resp = httpClient.execute(del);
                    String respStr = EntityUtils.toString(resp.getEntity());

                    //Creamos el objeto JSON
                    JSONObject respJSON = new JSONObject(respStr);
                    //Obtenemos valores del objeto JSON para su uso
                    String nombreAl = respJSON.getString("nombre");
                    String caloriasAl = respJSON.getString("calorias");
                    System.out.println("Devuelve: " + nombreAl + " - " + caloriasAl + " - ");

                    resul = true;

                    if (codigoAl == null) {//Para cuando pedimos todos los alimentos
                        JSONArray jsonAl = new JSONArray(respJSON);
                        //return jsonAl;
                    }

                } catch (Exception ex) {
                    Log.e("ServicioRest", "Error!", ex);
                }

                return resul;
            }


            protected void onPostExecute(Boolean result) {

                if (result) {

                }
            }
        }
    }
