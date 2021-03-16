package com.example.exament2moviles;

public class PuntoRuta {

    double latitud, longitud;
    String time;

    public PuntoRuta(){

    }

    public PuntoRuta(double latitud, double longitud, String time) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.time = time;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PuntoRuta{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                ", time=" + time +
                '}';
    }
}
