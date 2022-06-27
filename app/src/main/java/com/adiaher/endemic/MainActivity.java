package com.adiaher.endemic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private  static  String url="http://www.vculhbvl.lucusprueba.es/mostrar.php";
    List<FloraItem> floraItemList;
    RecyclerView  recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        floraItemList =new ArrayList<>();

        cargargarImagen();
    }


    private void cargargarImagen() {
        StringRequest stringRequest =new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Productos = array.getJSONObject(i);

                                floraItemList.add(new FloraItem(
                                        Productos.getString("id"),
                                        Productos.getString("nombre"),
                                        Productos.getString("nombreCientifico"),
                                        Productos.getString("habitat"),
                                        Productos.getString("notas"),
                                        Productos.getString("imagen")
                                ));
                            }
                            Adapter adapter = new Adapter(MainActivity.this, floraItemList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void agregar(View view) {
        startActivity(new Intent(getApplicationContext(), FloraAdd.class));
    }
}




