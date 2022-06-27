package com.adiaher.endemic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    String urlDelete = "http://www.vculhbvl.lucusprueba.es/eliminarEndemic.php";

    private ImageView img;
    private Button btnDelete;
    private TextView tvname, tvnamecien, tvhabitat, tvnotas;
    private FloraItem itemDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        initViews();
        initValues();
    }

    private void initViews() { //enlazar vistas con las variables de la pantalla Detalle
        img = findViewById(R.id.imagenDetalle);
        tvname = findViewById(R.id.txtname);
        tvnamecien = findViewById(R.id.txt_nameCien);
        tvhabitat = findViewById(R.id.txt_habitat);
        tvnotas = findViewById(R.id.txt_notas);
    }

    private void initValues() { // inicializar variables de pantalla Detalle
        itemDetail = (FloraItem) getIntent().getExtras().getSerializable("itemDetail");

        Glide.with(this).load(itemDetail.getImagen()).into(img); // pasar la url de la imagen en String a ImageView

        tvname.setText(itemDetail.getNombre());
        tvnamecien.setText(itemDetail.getNombreCientifico());
        tvhabitat.setText(itemDetail.getHabitat());
        tvnotas.setText(itemDetail.getNotas());


    }

    private void deleteFlora(final String id) { //método para eliminar el FloraItem en nuestra BBDD

        StringRequest request = new StringRequest(Request.Method.POST, urlDelete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("datos eliminados")) {
                            Toast.makeText(DetailActivity.this, "eliminado correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void btnDelete(View v) { // botón de eliminar que llama al método deleteFlora
        deleteFlora(itemDetail.getId());
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}