package com.example.uaqwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.util.HashMap;
import java.util.Map;

public class Compra extends AppCompatActivity {

    Button compra;
    EditText cantidad, articulo, codigoCom;
    FirebaseAuth mAuth;
    private FirebaseFirestore mfirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        compra = findViewById(R.id.btnRegistrarCompra);
        cantidad = findViewById(R.id.editTCantidad);
        articulo = findViewById(R.id.editTArticulo);
        codigoCom = findViewById(R.id.editTCodigo);

        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();

        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cant, arti, Ccompra;

                cant = String.valueOf(cantidad.getText());
                arti = String.valueOf(articulo.getText());
                Ccompra = String.valueOf(codigoCom.getText());


                registerBug(cant, arti, Ccompra);
            }
        });



    }

    private void registerBug(String cant, String arti, String ccompra) {

        String userId = mAuth.getCurrentUser().getUid();

        DocumentReference purchaseRef = mfirestore.collection("shoping").document();

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("userId", userId); // ID del usuario que realiza la compra
        purchaseData.put("cantidadArticulos", cant); // Cantidad de artículos
        purchaseData.put("codigoCompra", ccompra); // Código de la compra
        purchaseData.put("articulo", arti); // articulo comprado

        purchaseRef.set(purchaseData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Operación exitosa
                        // Aquí puedes realizar cualquier acción adicional, como mostrar un mensaje de éxito
                        Toast.makeText(Compra.this, "Compra registrada correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ocurrió un error al guardar los datos de la compra
                        // Aquí puedes manejar el error, como mostrar un mensaje de error al usuario
                        Toast.makeText(Compra.this, "Error al registrar la compra", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}