package com.adiaher.endemic;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class FloraAdd extends AppCompatActivity {

    String url = "http://www.vculhbvl.lucusprueba.es/insertarEndemic.php";
    String URLimgDefecto = "https://www.vculhbvl.lucusprueba.es/imagenes/defecto.png";
    EditText txtNombre, txtNombreCien, txtHabitat, txtNotas;
    ImageView iv;
    Button btn_insert, btnBuscar;

    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        //EDIT TEXT
        txtNombre = findViewById(R.id.nombre);
        txtNombreCien = findViewById(R.id.nomcien);
        txtHabitat = findViewById(R.id.habitat);
        txtNotas = findViewById(R.id.notas);

        // IMAGEN
        iv = findViewById(R.id.imageView);

        //BOTONES
        btn_insert = findViewById(R.id.btnInsert);
        btnBuscar = findViewById(R.id.btnBuscar);

        //Botón para agregar el FloraItem a nuestra BBDD
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertFlora();
            }
        });

        //Botón para buscar la imagen en nuestra galería
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    /////////////////IMAGEN/////////////////
    public String getStringImagen(Bitmap bmp) { // Pasar la imagen seleccionada en la galería a tipo String
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
    }

    @NonNull
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                // obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void insertFlora() {
        final String nombre = txtNombre.getText().toString().trim();
        final String nomcien = txtNombreCien.getText().toString().trim();
        final String habitat = txtHabitat.getText().toString().trim();
        final String notas = txtNotas.getText().toString().trim();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("cargando...");
            progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            if (response.equalsIgnoreCase("Se ha registrado correctamente en la BBDD")) {
                                Toast.makeText(FloraAdd.this, "Se ha registrado correctamente en la BBDD", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                progressDialog.dismiss();
                                finish();
                            } else {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FloraAdd.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    String imagen = getStringImagen(bitmap);

                    Map<String, String> params = new Hashtable<String, String>();

                    params.put("nombre", nombre);
                    params.put("nombreCientifico", nomcien);
                    params.put("habitat", habitat);
                    params.put("notas", notas);
                    params.put("imagen", imagen);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}