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

        private String email;
        private String password;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;


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
