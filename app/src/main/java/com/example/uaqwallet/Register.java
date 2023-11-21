package com.example.uaqwallet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    //Creamos las variables y los componentes que corresponden
    TextInputEditText editTextEmail, editTextPassword;
    EditText nameUs, expUser, facultadUser, noTarjeta;
    Button buttonReg;
    FirebaseAuth mAuth;
    TextView textView;
    private FirebaseFirestore mfirestore;

    @Override
    public void onStart() {
        super.onStart();
        // Revisar si el usuario tiene la sesion abierta
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();

        //Instanciar nuestrar variables con nuestros componentes y sus ID's
        setContentView(R.layout.activity_register);


        ProgressBar progressBar;

        editTextEmail = findViewById(R.id.emailRe);
        nameUs = findViewById(R.id.nameUser);
        expUser = findViewById(R.id.exped);
        facultadUser = findViewById(R.id.facultadUser);
        noTarjeta = findViewById(R.id.noTarjeta);
        editTextPassword = findViewById(R.id.passwordRe);
        buttonReg = findViewById(R.id.btn_aceptar);
        progressBar = findViewById(R.id.progressBarRe);
        textView = findViewById(R.id.iniciarSe);

        //Oyente de accion para la palabra inicia sesion
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamamos a la activity de Login y cerramos esta.
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        //Oyente de accion para el boton de "Registrar"
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password, nameU, expe, facu, noTargett;

                //Optenemos los datos del usuario que ingreso en los editText
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                nameU = nameUs.getText().toString().trim();
                expe = expUser.getText().toString().trim();
                facu = facultadUser.getText().toString().trim();
                noTargett = noTarjeta.getText().toString().trim();

                //Evitamos que esten vacios los campo
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && nameU.isEmpty() && expe.isEmpty() && facu.isEmpty() && noTargett.isEmpty()){
                    Toast.makeText(Register.this,"Llene todos los datos", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //Si los datos estan llenos mandamos llamar la funcion de registro
                    registerUser(email, password, nameU, expe, facu, noTargett);
                }

                //Mostramos la barra de progreso
                progressBar.setVisibility(view.VISIBLE);
                }
        });
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void registerUser(String email, String password, String nameU, String expe, String facu, String noTargett) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                int pnutos = 0;
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("nombre", nameU);
                map.put("correo", email);
                map.put("password", password);
                map.put("expediente", expe);
                map.put("facultad", facu);
                map.put("tarjeta", noTargett);
                map.put("puntos", pnutos);

                mfirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(new Intent(Register.this, MainActivity.class));
                        Toast.makeText(Register.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       Toast.makeText(Register.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}