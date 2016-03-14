package com.cro.encuestas;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cro.encuestas.bean.CustomRequest;
import com.cro.encuestas.bean.PreguntaBean;
import com.cro.encuestas.bean.RespuestaBean;
import com.cro.encuestas.bean.VolleyS;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncuestaActivity extends AppCompatActivity {

    ArrayList<PreguntaBean> listaPregunta;
    ArrayList<RespuestaBean> listaRespuesta;
    ArrayList<RadioButton> listaRadioBtn;
    ArrayList<CheckBox> listaCheckBox;
    private int idCorrelativo = 11;

    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    //final String url="";
    //final JSONObject jsonBody = new JSONObject("{\"type\":\"example\"}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        listaRadioBtn = new ArrayList<>();
        listaCheckBox = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        volley = VolleyS.getInstance(getApplicationContext());
        fRequestQueue = volley.getRequestQueue();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valor = "";
                for (int i = 0; i < listaRadioBtn.size(); i++) {
                    RadioButton radito = listaRadioBtn.get(i);
                    valor = (String) radito.getText();
                    Toast.makeText(getApplicationContext(), "radio: " + valor, Toast.LENGTH_SHORT).show();
                    //Se valida si hizo checked
                    if (radito.isChecked()) {
                        Toast.makeText(getApplicationContext(), "radio con check: " + valor, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), valor + "respuestaID: " + radito.getId(), Toast.LENGTH_SHORT).show();
                    }
                }

                for (int x = 0; x < listaCheckBox.size(); x++) {
                    CheckBox chekado = listaCheckBox.get(x);
                    valor = (String) chekado.getText();
                    Toast.makeText(getApplicationContext(), "check: " + valor, Toast.LENGTH_SHORT).show();
                    //Se valida si hizo checked
                    if (chekado.isChecked()) {
                        Toast.makeText(getApplicationContext(), "check con check: " + valor, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), valor + "respuestaID: " + chekado.getId(), Toast.LENGTH_SHORT).show();
                    }

                }

                makeRequest();
            }
        });

        new getDataPreguntas().execute("http://www.perucomputo.com/webservices/pregunta.php");
        new getDataRespuestas().execute("http://www.perucomputo.com/webservices/respuesta.php");

        String url = "http://www.perucomputo.com/webservices/grabarencuesta.php";
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "Droider");

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("ResponseXXXXX: ", response.toString());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        addToQueue(jsObjRequest);

    }

    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (fRequestQueue == null)
                fRequestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            onPreStartConnection();
            fRequestQueue.add(request);
        }
    }
    //Volley
    public void onPreStartConnection() {
        this.setProgressBarIndeterminateVisibility(true);
    }
    //Volley
    public void onConnectionFinished() {
        this.setProgressBarIndeterminateVisibility(false);
    }
    //Volley
    public void onConnectionFailed(String error) {
        this.setProgressBarIndeterminateVisibility(false);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
    //Volley
    private void makeRequest(){
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=39.476245,-0.349448&sensor=true";
        String url2 = "http://www.perucomputo.com/webservices/pregunta.php";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_SHORT).show();
                onConnectionFinished();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onConnectionFailed(volleyError.toString());
            }
        });

        JsonArrayRequest request2 = new JsonArrayRequest(url2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_SHORT).show();
                onConnectionFinished();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onConnectionFailed(volleyError.toString());
            }
        });
        addToQueue(request);
        addToQueue(request2);


    }



    private void cargarPreguntasRespuestas() {
        PreguntaBean objPregunta;

        for (int i = 0; i < listaPregunta.size(); i++) {
            objPregunta = new PreguntaBean();
            objPregunta = listaPregunta.get(i);
            addTextViewPregunta(objPregunta.getDescripcion());
            boolean esRadio = objPregunta.getTipoRespuestaID() == 2 ? true : false;
            addRadioCheckboxButtonRespuesta(objPregunta.getPreguntaID(), esRadio);
        }
    }

    public class getDataPreguntas extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();
            String urlString = args[0];

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
                try {
                    JSONArray array = new JSONArray(result);
                    listaPregunta = new ArrayList<PreguntaBean>();
                    PreguntaBean objPregunta;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        objPregunta = new PreguntaBean();
                        objPregunta.setPreguntaID(row.getInt("PreguntaID"));
                        objPregunta.setEncuestaID(row.getInt("EncuestaID"));
                        objPregunta.setDescripcion(row.getString("Descripcion"));
                        objPregunta.setTipoRespuestaID(row.getInt("TipoRespuestaID"));
                        listaPregunta.add(objPregunta);
                    }
                    //Se recorre para listar las preguntas
                    //CargarPreguntas();

                    /*Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "Valor: " + row.getString("Descripcion"), Toast.LENGTH_SHORT);
                    toast1.show();*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end
        }
    }

    public class getDataRespuestas extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();
            String urlString = args[0];

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
                try {
                    JSONArray array = new JSONArray(result);
                    listaRespuesta = new ArrayList<RespuestaBean>();
                    RespuestaBean objRespuesta;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        objRespuesta = new RespuestaBean();
                        objRespuesta.setPreguntaID(row.getInt("PreguntaID"));
                        objRespuesta.setRespuestaID(row.getInt("RespuestaID"));
                        objRespuesta.setDescripcion(row.getString("Descripcion"));
                        listaRespuesta.add(objRespuesta);
                    }

                    //Se recorre para listar las preguntas
                    //CargarRespuestas();
                    cargarPreguntasRespuestas();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end
        }
    }

    public void addTextViewPregunta(String nomPregunta) {
        Log.w("IDCorrelativo", Integer.toString(idCorrelativo));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_example);
        // Add textview 1
        TextView textView1 = new TextView(this);
        //textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
        //      LayoutParams.WRAP_CONTENT));
        textView1.setText(nomPregunta.toUpperCase());
        textView1.setId(idCorrelativo + 1);
        //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
        textView1.setTextSize(26);
        textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
        textView1.setGravity(Gravity.LEFT);
        linearLayout.addView(textView1);
        idCorrelativo++;
    }

    public void addRadioCheckboxButtonRespuesta(int idPregunta, boolean esRadio) {

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_example);
        RespuestaBean resp;
        ArrayList<RespuestaBean> respuestasPreguntas = new ArrayList<RespuestaBean>();
        for (int i = 0; i < listaRespuesta.size(); i++) {
            resp = new RespuestaBean();
            resp = listaRespuesta.get(i);
            if (resp.getPreguntaID() == idPregunta) {
                respuestasPreguntas.add(resp);
            }
        }
        if (esRadio) {
            RadioGroup rgp = new RadioGroup(this);
            rgp.setId(idPregunta);
            rgp.setTag("rgp" + idPregunta);
            for (int i = 0; i < respuestasPreguntas.size(); i++) {
                resp = new RespuestaBean();
                resp = respuestasPreguntas.get(i);
                //Se crean todas las respuestas de las preguntas
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setTag(resp.getRespuestaID());
                rdbtn.setId(resp.getRespuestaID());
                rdbtn.setText(resp.getDescripcion());
                rgp.addView(rdbtn);
                listaRadioBtn.add(rdbtn);
            }
            linearLayout.addView(rgp);
        } else {
            for (int i = 0; i < respuestasPreguntas.size(); i++) {
                resp = new RespuestaBean();
                resp = respuestasPreguntas.get(i);
                //Se crean todas las respuestas de las preguntas
                CheckBox chkResp = new CheckBox(this);
                chkResp.setTag(resp.getRespuestaID());
                chkResp.setId(resp.getRespuestaID());
                chkResp.setText(resp.getDescripcion());
                linearLayout.addView(chkResp);
                listaCheckBox.add(chkResp);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_encuesta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}