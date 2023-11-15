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

        //Instanciar nuestrar variables con nuestros componentes y sus ID's
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();

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

                //Evitamos que esten vacios los campos
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this,"Ingresa un correo", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this,"Ingresa una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (nameU.isEmpty() && expe.isEmpty() && facu.isEmpty() && noTargett.isEmpty()){
                    Toast.makeText(Register.this,"Llene todos los datos", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> map = new HashMap<>();
                map.put("nombre", nameU);
                map.put("expediente", expe);
                map.put("facultad", facu);
                map.put("tarjeta", noTargett);

                mfirestore.collection("Users").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Register.this,"Datos agregados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                //Mostramos la barra de progreso
                progressBar.setVisibility(view.VISIBLE);
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
                //Mandamos los datos a la base de datos para poder iniciar sesion despues
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(view.GONE);
                                if (task.isSuccessful()) {
                                    //La cuenta se creo de manera correcta
                                    Toast.makeText(Register.this, "Cuenta creada.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    //La cuenta no se creo de manera correcta.
                                    Toast.makeText(Register.this, "Error en crear la cuenta.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}