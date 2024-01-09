package es.uniovi.converter;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private double mEuroToDollar;
    private EditText mEditTextEuros;
    private EditText mEditTextDollars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEuroToDollar = 1.16;
        mEditTextEuros = (EditText) findViewById(R.id.editTextEuros);
        mEditTextDollars = (EditText) findViewById(R.id.editTextDollars);

        // Crear la cola de peticiones HTTP
        RequestQueue queue = Volley.newRequestQueue(this);
        // Crear una petición
        /*String url ="http://api.exchangerate.host/convert?from=EUR&to=USD&amount=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                // Implementar el interfaz Listener que debe tener el método
                // onResponse, que será llamado al recibir la respuesta del servidor
                // Este método se ejecutará en el hilo del GUI
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Volley: recibido: "+ response);
                    }
                },
                // Implementar el interfaz ErrorListener, que debe tener el método
                // onErrorResponse, que será llamado si la respuesta del servidor no es 200 OK
                // También se ejecutará en la interfaz de usuario
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Volley: ha ocurrido un error " + error);
                    }
                }
        );*/

        // Crear una petición con JSON
        String url = "http://api.exchangerate.host/convert?from=EUR&to=USD&amount=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mEuroToDollar = response.getDouble("result");
                            System.out.println("El factor de conversión es: " + Double.toString(mEuroToDollar));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Volley: ha ocurrido un error " + error);
                    }
                }
        );
        // Pedirle a Volley que no use respuestas previamente cacheadas
        //stringRequest.setShouldCache(false);
        // Y poner la petición en la cola
        //queue.add(stringRequest);
        jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);
    }

    public void onClickToDollars(View view) {
        convert(mEditTextEuros, mEditTextDollars, mEuroToDollar);
        Toast.makeText(getApplicationContext(), "Me han pulsado",
                Toast.LENGTH_SHORT).show();
    }

    public void onClickToEuros(View view) {
        convert(mEditTextDollars, mEditTextEuros, mEuroToDollar);
        Toast.makeText(getApplicationContext(), "Me han pulsado",
                Toast.LENGTH_SHORT).show();
    }

    void convert(EditText editTextSource, EditText editTextDestination,
                 double ConversionFactor) {

        String StringSource = editTextSource.getText().toString();

        double NumberSource;
        try {
            NumberSource = Double.parseDouble(StringSource);
        } catch (NumberFormatException nfe) {
            return;
        }
        double NumberDestination = NumberSource * ConversionFactor;
        String StringDestination = Double.toString(NumberDestination);
        editTextDestination.setText(StringDestination);
    }
}