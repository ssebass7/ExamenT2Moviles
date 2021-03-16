package com.example.exament2moviles;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap miMapa;
    private Button btn_posicion;
    private PolylineOptions ruta = new PolylineOptions();
    private boolean mapaCentrado = false;
    private LocationListener oyente;
    private  LocationManager lm;
    private DatabaseReference myRef;
    private Timestamp time;
    Marker marcador;
    Boolean condicion;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://exament2android-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("Puntos");
        btn_posicion = findViewById(R.id.btn_posicion);
        btn_posicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                condicion = true;
                pedirActualizaciones();
            }
        });

        chekearPermiso();
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        miMapa = googleMap;
        //Habilitar boton

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //pedirActualizaciones();
                btn_posicion.setEnabled(true);
            }
        }else{
            btn_posicion.setEnabled(false);
        }
    }


    public void pedirActualizaciones() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        oyente = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                while(condicion == true){
                    condicion = meterNuevoPuntoEnRuta(location);
                }

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, oyente);



    }



    private boolean meterNuevoPuntoEnRuta(Location location) {

        miMapa.clear();
        LatLng punto = new LatLng(location.getLatitude(),location.getLongitude());
        DateFormat formato = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        Date date = new Date(location.getTime());
        String formateado = formato.format(date);


        PuntoRuta puntoRuta = new PuntoRuta();
        puntoRuta.setLatitud(punto.latitude);
        puntoRuta.setLongitud(punto.longitude);
        puntoRuta.setTime(formateado);

        marcador = miMapa.addMarker(new MarkerOptions().position(punto).title("Punto de Localizacion"));
        marcador.setTag("Posicion");
        if (mapaCentrado==false){
            miMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, 8));
            mapaCentrado = true;
        }

        //Centro el mapa en el punto que me ha llegado
        String nombreRuta = ponerNombreRuta();
        myRef.child(nombreRuta).setValue(puntoRuta);
        miMapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent i = new Intent(MapsActivity.this, VistaPuntos.class);
                startActivity(i);


                return true;

            }
        });

        return condicion = false;
    }


    public String ponerNombreRuta(){

        String numAletatorio = String.valueOf(Math.random());
        numAletatorio = numAletatorio.replace(".","");

        String nombreRuta = "Punto" + numAletatorio;

        return nombreRuta;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void chekearPermiso()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            btn_posicion.setEnabled(false);

        }
        else{
            Log.d("PERMISO","Ya tengo permiso y no hago nada!!");
            btn_posicion.setEnabled(true);
        }
    }
}
