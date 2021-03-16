package com.example.exament2moviles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MiAdaptador extends RecyclerView.Adapter<com.example.exament2moviles.MiAdaptador.MiContenedorDeVistas> {
    private ArrayList<PuntoRuta> lista_puntos;
    public MiAdaptador(ArrayList<PuntoRuta> lista_puntos) {
        this.lista_puntos = lista_puntos;
    }
    @NonNull
    @Override
    public com.example.exament2moviles.MiAdaptador.MiContenedorDeVistas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_recycler, parent, false);
        MiContenedorDeVistas contenedor = new MiContenedorDeVistas(vista);
        return contenedor;
    }



    @Override
    public void onBindViewHolder(@NonNull com.example.exament2moviles.MiAdaptador.MiContenedorDeVistas holder, int position) {
        PuntoRuta punto = lista_puntos.get(position);
        String str_latitud = String.valueOf(punto.getLongitud());
        String str_longitud = String.valueOf(punto.getLongitud());
        String str_time = String.valueOf(punto.getTime());
        holder.latitud.setText(str_latitud);
        holder.longitud.setText(str_longitud);
        holder.timestamp.setText(str_time);
    }

    @Override
    public int getItemCount() {
        return lista_puntos.size();
    }

    public static class MiContenedorDeVistas extends RecyclerView.ViewHolder{
        public TextView latitud, longitud, timestamp;

        public MiContenedorDeVistas(View vista) {
            super(vista);
            this.latitud = vista.findViewById(R.id.txt_latitud);
            this.longitud = vista.findViewById(R.id.txt_longitud);
            this.timestamp = vista.findViewById(R.id.txt_timestamp);
        }
    }
}