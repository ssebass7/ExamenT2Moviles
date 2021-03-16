package com.example.exament2moviles;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VistaPuntos extends AppCompatActivity {
    ArrayList<PuntoRuta> lista=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_contactos);


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://exament2android-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Puntos");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for(DataSnapshot d: data){
                    PuntoRuta punto = d.getValue(PuntoRuta.class);
                    lista.add(punto);
                    Log.d("bi", lista.toString());
                    mostrar();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("bi", error.getDetails());
            }
        });


    }
    private void mostrar(){
        RecyclerView recc = findViewById(R.id.recycler);
        RecyclerView.LayoutManager gestor = new LinearLayoutManager(this);
        MiAdaptador adaptador = new MiAdaptador(lista);
        recc.setLayoutManager(gestor);
        recc.setAdapter(adaptador);
    }
}