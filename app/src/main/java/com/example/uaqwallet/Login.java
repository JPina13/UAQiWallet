package com.example.uaqwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLog;
    FirebaseAuth mAuth;
    TextView textView;

    //Revisar si el usuario dejo la sesion abierta en cuyo caso no es necesario volver a mostrar la activity
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //Si la sesion no se cerro se manda a la activity principal
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//Crear nuestrar variables junto con sus instancias en el xml
        mAuth = FirebaseAuth.getInstance();
        ProgressBar progressBar;
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLog = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBarLog);
        textView = findViewById(R.id.registrar);

        //Oyente de accion paraa la palabra "registrate" donde se manda llmar la activity de Registrar
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamada a la activity
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                finish();
            }
        });

        //Oyente de accion para el boton de "iniciar sesion"
        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password;

                //Optenemos los datos que el usuario ingreso en los editText
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                //En caso de estar vacio los campos es necesario mostrar el mensaje de llenarlo
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this,"Ingresa un correo", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this,"Ingresa una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Mostramos la barra de progreso
                progressBar.setVisibility(view.VISIBLE);
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // Comprobar los datos en la base de datos de Firebase
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(view.GONE);
                                //Si los datos son correctos le damos el mensaje de bienvenida
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Bienvenido!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {//Si no le mostramos que hubo un error
                                    Toast.makeText(Login.this, "Autenticación fallida.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}