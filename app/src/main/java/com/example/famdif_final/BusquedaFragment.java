package com.example.famdif_final;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BusquedaFragment extends BaseFragment {

    private TipoTienda tipoTiendaLista;
    private SubtipoTienda subtipoTiendaLista;

    private Spinner tipoTienda;
    private Spinner subtipoTienda;
    private Spinner despDistancia;
    private Spinner despAccesibilidad;

    private ArrayAdapter adapt;
    private ArrayAdapter adapt1;
    private ArrayAdapter adapt2;
    private ArrayAdapter adapt3;

    private String tTienda;
    private String stTienda;
    private String distSeleccionada;
    private String acceSeleccionada;

    private TextView nombreTienda;
    private TextView direccion;
    private List<String> dist= Arrays.asList("100","200","500","1000","CUALQUIERA");
    private List<String> accesibilidad=Arrays.asList("ACCESIBLE", "ACCESIBLE CON DIFICULTAD","PRACTICABLE CON AYUDA","CUALQUIERA");
    private Button busqueda;
    private DatabaseReference resultado;
    private List<Tienda> tiendasEncontradas= new ArrayList<>();

    private static double latitudActual;
    private static double longitudActual;

    private int tipoBusqueda;




    public BusquedaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setMainActivity((MainActivity) getActivity());

        getMainActivity().getSupportActionBar().setTitle("BUSQUEDA");
        final View view=inflater.inflate(R.layout.fragment_busqueda, container, false);

        tipoTienda=view.findViewById(R.id.desplegableTipoEstablecimiento);
        subtipoTienda=view.findViewById(R.id.desplegablesubtipoEstablecimiento);
        despDistancia=view.findViewById(R.id.desplegableDistancia);
        despAccesibilidad=view.findViewById(R.id.desplegableAccesibilidad);
        busqueda=view.findViewById(R.id.btnBusqueda);
        nombreTienda=view.findViewById(R.id.textoNombreTienda);
        direccion=view.findViewById(R.id.textoDireccion);

        getUbicacion();


        tipoTiendaLista = new TipoTienda();
        subtipoTiendaLista = new SubtipoTienda("Alimentacion");

        adapt = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,tipoTiendaLista.getTiposTienda());
        tipoTienda.setAdapter(adapt);


        adapt1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,subtipoTiendaLista.getSubTiposTienda());
        subtipoTienda.setAdapter(adapt1);

        adapt2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, dist);
        despDistancia.setAdapter(adapt2);

        adapt3 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,accesibilidad);
        despAccesibilidad.setAdapter(adapt3);

        MainActivity.controlador.setShops(null);

        tipoTienda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tTienda = (String) tipoTienda.getAdapter().getItem(position);
                SubtipoTienda subtTienda = new SubtipoTienda(tTienda);
                ArrayAdapter adapt4 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,subtTienda.getSubTiposTienda());
                subtipoTienda.setAdapter(adapt4);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subtipoTienda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stTienda = (String) subtipoTienda.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        despAccesibilidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                acceSeleccionada = (String) despAccesibilidad.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        despDistancia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                distSeleccionada = (String) despDistancia.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Tipo, subtipo, nombre, direccion, maxDistancia, accesibilidad minima
        busqueda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tiendasEncontradas.clear();
                if(tTienda.matches("Cualquiera")){
                    busqueda2(despDistancia.getSelectedItem().toString(),despAccesibilidad.getSelectedItem().toString());
                //}else if(nombreTienda.getText().toString().matches("") && direccion.getText().toString().matches("")){
                    //busqueda1(tTienda,stTienda,despDistancia.getSelectedItem().toString(),despAccesibilidad.getSelectedItem().toString());
                }else{
                    busqueda3(tTienda,stTienda,despDistancia.getSelectedItem().toString(),despAccesibilidad.getSelectedItem().toString());
                }

            }
        });

        return view;
    }

    private void busqueda3(String tTienda, String stTienda, String distancia, String accesMin) {
        getUbicacion();
        int accTemp=parsearAccesibilidadTextoNumero(accesMin);

        if(!nombreTienda.getText().toString().matches("") && !direccion.getText().toString().matches(""))
            tipoBusqueda=2;
        else if(!nombreTienda.getText().toString().matches("") && direccion.getText().toString().matches(""))
            tipoBusqueda=1;
        else if(nombreTienda.getText().toString().matches("") && !direccion.getText().toString().matches(""))
            tipoBusqueda=3;
        else if(nombreTienda.getText().toString().matches("") && direccion.getText().toString().matches(""))
            tipoBusqueda=4;

        MainActivity.db.collection("comerciosElCarmenTest")
                .whereEqualTo("tipo", tTienda)
                .whereEqualTo("subtipo", stTienda)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && tipoBusqueda == 2){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Tienda tienda = document.toObject(Tienda.class);
                                if(tienda.getNombre().contains(nombreTienda.getText().toString()) &&
                                        tienda.getDireccion().contains(direccion.getText().toString())){
                                    if (distancia.matches("CUALQUIERA") && accTemp == 4) {
                                        tiendasEncontradas.add(tienda);
                                    } else if (distancia.matches("CUALQUIERA") && accTemp != 4) {
                                        if (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp) {
                                            tiendasEncontradas.add(tienda);
                                        }
                                    } else {
                                        if ((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }
                                }
                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if (tiendasEncontradas.size() > 0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }else if(task.isSuccessful() && tipoBusqueda == 1){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Tienda tienda = document.toObject(Tienda.class);
                                if(tienda.getNombre().contains(nombreTienda.getText().toString())){
                                    if (distancia.matches("CUALQUIERA") && accTemp == 4) {
                                        tiendasEncontradas.add(tienda);
                                    } else if (distancia.matches("CUALQUIERA") && accTemp != 4) {
                                        if (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp) {
                                            tiendasEncontradas.add(tienda);
                                        }
                                    } else {
                                        if ((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }
                                }
                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if (tiendasEncontradas.size() > 0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }else if(task.isSuccessful() && tipoBusqueda == 3){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Tienda tienda = document.toObject(Tienda.class);
                                if(tienda.getDireccion().contains(direccion.getText().toString())){
                                    if (distancia.matches("CUAKQUIERA") && accTemp == 4) {
                                        tiendasEncontradas.add(tienda);
                                    } else if (distancia.matches("CUALQUIERA") && accTemp != 4) {
                                        if (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp) {
                                            tiendasEncontradas.add(tienda);
                                        }
                                    } else {
                                        if ((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }
                                }

                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if (tiendasEncontradas.size() > 0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }else if(task.isSuccessful() && tipoBusqueda == 4){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Tienda tienda = document.toObject(Tienda.class);
                                    if (distancia.matches("CUALQUIERA") && accTemp == 4) {
                                        tiendasEncontradas.add(tienda);
                                    } else if (distancia.matches("CUALQUIERA") && accTemp != 4) {
                                        if (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp) {
                                            tiendasEncontradas.add(tienda);
                                        }
                                    } else {
                                        if ((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }

                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if (tiendasEncontradas.size() > 0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }

                    }

                });


    }


    public double distanciaCoord(double lat1, double lng1, double lat2, double lng2) {
        getUbicacion();
        Log.i("lat1,lon1,lat2,lon2", String.valueOf(lat1)+";"+String.valueOf(lng1)+";"+String.valueOf(lat2)+";"+String.valueOf(lng2));
        double radioTierra = 6371000;//en kilómetros
        double dLat = Math.toRadians(lat2 - latitudActual);
        double dLng = Math.toRadians(lng2 - longitudActual);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2;

        Log.i("Calculando distancia: ", String.valueOf(distancia));
        return distancia;
    }

    /*
    private void busqueda1(String tipo, String subtipo, String distancia, String accesMin) {
        getUbicacion();
        int accTemp=parsearAccesibilidadTextoNumero(accesMin);
        if((nombreTienda.getText().toString().matches("")) && (direccion.getText().toString().matches(""))){
            MainActivity.db.collection("comerciosElCarmenTest")
                    .whereEqualTo("tipo", tTienda)
                    .whereEqualTo("subtipo", stTienda)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Tienda tienda = document.toObject(Tienda.class);
                                    if (distancia.matches("Cualquiera") && accTemp == 4) {
                                        tiendasEncontradas.add(tienda);
                                    } else if (distancia.matches("Cualquiera") && accTemp != 4) {
                                        if (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp) {
                                            tiendasEncontradas.add(tienda);
                                        }
                                    } else {
                                        if ((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }
                                }
                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if (tiendasEncontradas.size() > 0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }

                    });
        }else if((!nombreTienda.getText().toString().matches("")) && (direccion.getText().toString().matches(""))){
            MainActivity.db.collection("comerciosElCarmenTest")
                    .whereEqualTo("tipo", tTienda)
                    .whereEqualTo("subtipo", stTienda)
                    .whereArrayContains("nombre",nombreTienda.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Tienda tienda = document.toObject(Tienda.class);
                                    if (distancia.matches("Cualquiera") && accTemp == 4) {
                                        Log.i("CASO_1", "ENtramos en el caso 1");
                                        tiendasEncontradas.add(tienda);
                                    } else if (distancia.matches("Cualquiera") && accTemp != 4) {
                                        if (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp) {
                                            Log.i("CASO_2", "ENtramos en el caso 2");
                                            tiendasEncontradas.add(tienda);
                                        }
                                    } else {
                                        if ((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }
                                }
                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if (tiendasEncontradas.size() > 0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }
                    });
        }else if((nombreTienda.getText().toString().matches("")) && (!direccion.getText().toString().matches(""))){
            MainActivity.db.collection("comerciosElCarmenTest")
                    .whereEqualTo("tipo", tTienda)
                    .whereEqualTo("subtipo", stTienda)
                    .whereArrayContains("direccion",nombreTienda.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Tienda tienda = document.toObject(Tienda.class);
                                    if (distancia.matches("Cualquiera") && accTemp == 4) {
                                        Log.i("CASO_1", "ENtramos en el caso 1");
                                        tiendasEncontradas.add(tienda);
                                    } else if (distancia.matches("Cualquiera") && accTemp != 4) {
                                        if (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp) {
                                            Log.i("CASO_2", "ENtramos en el caso 2");
                                            tiendasEncontradas.add(tienda);
                                        }
                                    } else {
                                        if ((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion()) <= accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }
                                }
                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if (tiendasEncontradas.size() > 0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }
                    });
        }
    }*/

    private void busqueda2(String distancia, String accesMin) {
        getUbicacion();
        int accTemp=parsearAccesibilidadTextoNumero(accesMin);
        if((!nombreTienda.getText().toString().matches("")) && (!direccion.getText().toString().matches(""))){
            MainActivity.db.collection("comerciosElCarmenTest")
                    .whereArrayContains("nombre",nombreTienda.getText().toString())
                    .whereArrayContains("direccion",direccion.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    Tienda tienda = document.toObject(Tienda.class);
                                    if(distancia.matches("CUALQUIERA") && accTemp==4){
                                        Log.i("CASO_1","ENtramos en el caso 1");
                                        tiendasEncontradas.add(tienda);
                                    }else if(distancia.matches("CUALQUIERA") && accTemp!=4){
                                        if(parsearAccesibilidadBBDD(tienda.getClasificacion())<=accTemp){
                                            Log.i("CASO_2","ENtramos en el caso 2");
                                            tiendasEncontradas.add(tienda);
                                        }
                                    }else{
                                        if((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion())<=accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }
                                }
                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if(tiendasEncontradas.size()>0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }
                    });
        }else if((!nombreTienda.getText().toString().matches("")) && (direccion.getText().toString().matches(""))){
            MainActivity.db.collection("comerciosElCarmenTest")
                    .whereGreaterThanOrEqualTo("nombre",nombreTienda.getText().toString().toUpperCase())
                    .whereLessThanOrEqualTo("nombre",nombreTienda.getText().toString().toUpperCase(Locale.ROOT)+'\uf8ff')
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    Tienda tienda = document.toObject(Tienda.class);
                                    if(distancia.matches("CUALQUIERA") && accTemp==4){
                                        tiendasEncontradas.add(tienda);
                                    }else if(distancia.matches("CUALQUIERA") && accTemp!=4){
                                        if(parsearAccesibilidadBBDD(tienda.getClasificacion())<=accTemp){
                                            tiendasEncontradas.add(tienda);
                                        }
                                    }else{
                                        if((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion())<=accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }
                                }
                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if(tiendasEncontradas.size()>0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }
                    });
        }else if((nombreTienda.getText().toString().matches("")) && (!direccion.getText().toString().matches(""))){
            MainActivity.db.collection("comerciosElCarmenTest")
                    .whereGreaterThanOrEqualTo("direccion",direccion.getText().toString().toUpperCase())
                    .whereLessThanOrEqualTo("direccion",direccion.getText().toString().toUpperCase(Locale.ROOT)+'\uf8ff')
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    Tienda tienda = document.toObject(Tienda.class);
                                    if(distancia.matches("CUALQUIERA") && accTemp==4){
                                        tiendasEncontradas.add(tienda);
                                    }else if(distancia.matches("CUALQUIERA") && accTemp!=4){
                                        if(parsearAccesibilidadBBDD(tienda.getClasificacion())<=accTemp){
                                            tiendasEncontradas.add(tienda);
                                        }
                                    }else{
                                        if((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion())<=accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }
                                }
                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if(tiendasEncontradas.size()>0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }
                    });
        }else{
            MainActivity.db.collection("comerciosElCarmenTest")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    //Log.i("Longitud",document.get("longitud").toString());
                                    Log.i("ID",document.get("id").toString());
                                    Log.i("Tienda",document.toString());
                                    Tienda tienda = document.toObject(Tienda.class);
                                    if(distancia.matches("CUALQUIERA") && accTemp==4){
                                        tiendasEncontradas.add(tienda);
                                    }else if(distancia.matches("CUALQUIERA") && accTemp!=4){
                                        if(parsearAccesibilidadBBDD(tienda.getClasificacion())<=accTemp){
                                            Log.i("Entramos aqui...","entrando");
                                            tiendasEncontradas.add(tienda);
                                        }
                                    }else{
                                        if((distanciaCoord(latitudActual, longitudActual, Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()))
                                                <= Double.valueOf(despDistancia.getSelectedItem().toString())) &&
                                                (parsearAccesibilidadBBDD(tienda.getClasificacion())<=accTemp)
                                        )
                                            tiendasEncontradas.add(tienda);
                                    }
                                }
                            }
                            Controlador.getInstance().setShops(tiendasEncontradas);
                            if(tiendasEncontradas.size()>0) {
                                getMainActivity().setFragment(FragmentName.MAP);
                            }
                        }
                    });
        }
    }



    @SuppressLint("MissingPermission")
    private void getUbicacion() {
        if (!getMainActivity().getGpsManager().checkPermissions()) {
            getMainActivity().getGpsManager().requestPermissions();

        } else {

            getMainActivity().getGpsManager().getFusedLocationProviderClient().getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @Override
                public boolean isCancellationRequested() {
                    return false;
                }

                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }
            }).addOnCompleteListener(getMainActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        latitudActual=task.getResult().getLatitude();
                        longitudActual=task.getResult().getLongitude();
                        Log.i("Ubicacion ",String.valueOf(latitudActual)+String.valueOf(longitudActual));
                    }
                }
            });
        }
    }

    private int parsearAccesibilidadTextoNumero(String accesMin){
        int accTemp;
        switch (accesMin){
            case "ACCESIBLE":
                accTemp=1;
                break;
            case "ACCESIBLE CON DIFICULTAD":
                accTemp=2;
                break;
            case "PRACTICABLE CON AYUDA":
                accTemp=3;
                break;
            case "CUALQUIERA":
                accTemp=4;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + accesMin);
        }
        return accTemp;
    }

    private int parsearAccesibilidadBBDD(String accesMin){
        int accTemp;
        switch (accesMin){
            case "A":
                accTemp=1;
                break;
            case "B":
                accTemp=2;
                break;
            case "C":
                accTemp=3;
                break;
            case "D":
                accTemp=4;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + accesMin);
        }
        return accTemp;
    }

}