package com.example.uaqwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {

    //Crear nuestras variables deacuerdo a los componentes que usamos
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asignar a las variables los componentes con sus ID's
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_detall);
        user = auth.getCurrentUser();

        //Comprobar que la sesion este abierta
        if (user == null){
            //Si no lo esta mandamos a la activity de "Login"
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }else{
            //Si lo esta obtenemos el correo del usuario ingresado en la base de datos
            textView.setText(user.getEmail());
        }

        //Oyenye de accion para el boton de cerrar la sesion
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();//Cerramos la sesion
                Intent intent = new Intent(getApplicationContext(), Login.class);//Llamamos a la activity de "Login"
                startActivity(intent);
                finish();
            }
        });
    }
}