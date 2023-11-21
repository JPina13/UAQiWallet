package com.example.uaqwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    //Crear nuestras variables deacuerdo a los componentes que usamos
    FirebaseAuth auth;
    Button button, adeudos, compras, canje;
    TextView textView, nameUser, facuUser, points;
    FirebaseUser user;
    FirebaseFirestore mfirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = mfirestore.collection("user");

        //Asignar a las variables los componentes con sus ID's
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        adeudos = findViewById(R.id.adeudos);
        compras = findViewById(R.id.btnCompras);
        canje = findViewById(R.id.btnCanjes);
        adeudos = findViewById(R.id.adeudos);
        textView = findViewById(R.id.user_detall);
        nameUser = findViewById(R.id.textname);
        facuUser = findViewById(R.id.textfacu);
        points = findViewById(R.id.textcantPuntos);
        user = auth.getCurrentUser();

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Comprobar que la sesion este abierta
        if (user == null){
            //Si no lo esta mandamos a la activity de "Login"
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }else{
            //Si lo esta obtenemos el correo del usuario ingresado en la base de datos
            textView.setText(user.getEmail());

            //Consultar la base de datos
            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String key = document.getId();
                            nameUser.setText(document.getString("nombre"));
                            facuUser.setText(document.getString("facultad"));
                        }
                    } else {
                        // Manejar errores
                    }
                }
            });

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

        //Oyente de accion del boton adeudos
        adeudos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Adeudos.class);//Llamamos a la activity de "Adeudos"
                startActivity(intent);
            }
        });

        //Oyente de accion para el boton de compras


        //Oyente de accion para el boton de canje

    }
}