package com.example.famdif_final;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    protected static FirebaseAuth mAuth;
    protected static FirebaseFirestore db;
    protected static DatabaseReference databaseReference;
    protected static StorageReference mStorageRef;

    protected static LocationManager locationManager;
    protected static LocationListener locationListener;
    protected static Location loca;

    protected static final int PICK_IMAGE_REQUEST=1;

    private GPSManager gpsManager;


    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://famdiffinal-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        gpsManager = new GPSManager(this);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.index_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_hamb);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout = findViewById(R.id.index_layout);

        NavigationView navigationView = findViewById(R.id.navigation_index_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        setupNavigationDrawerContent(navigationView);
        setFragment(FragmentName.INDEX);

    }


    public NavigationView getNavigationView() {
        return (NavigationView) findViewById(R.id.navigation_index_view);
    }

    public void setOptionMenu(int res) {
        getNavigationView().getMenu().findItem(res).setChecked(true);
    }

    public void changeMenu(MenuType menuType) {
        getNavigationView().getMenu().clear();
        switch (menuType) {
            case DISCONNECTED:
                getNavigationView().inflateMenu(R.menu.index_menu);
                break;
            case USER_LOGGED:
                getNavigationView().inflateMenu(R.menu.home_menu);
                break;
            case ADMIN_LOGGED:
                getNavigationView().inflateMenu(R.menu.admin_menu);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_index:
                                setFragment(FragmentName.INDEX);
                                break;

                            case R.id.item_log_in:
                                setFragment(FragmentName.LOG_IN);

                            case R.id.item_add_shop:
                                setFragment(FragmentName.ADD_SHOP);
                        }

                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );
    }


    public void setFragment(FragmentName options) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (options) {
            case INDEX:
                IndexFragment indexHomeFragment = new IndexFragment();
                fragmentTransaction.replace(R.id.index_fragment, indexHomeFragment);
                break;
            case SIGN_IN:
                RegistrarFragment signInFragment = new RegistrarFragment();
                fragmentTransaction.replace(R.id.index_fragment, signInFragment);
                break;
            case LOG_IN:
                AccederFragment logInFragment = new AccederFragment();
                fragmentTransaction.replace(R.id.index_fragment, logInFragment);
                break;
            case HOME:
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.index_fragment, homeFragment);
                break;
            case SEARCH:
                BusquedaFragment busquedaFragment = new BusquedaFragment();
                fragmentTransaction.replace(R.id.index_fragment, busquedaFragment);
                break;
            case SUGGESTIONS:
                SuggestionFragment suggestionFragment = new SuggestionFragment();
                fragmentTransaction.replace(R.id.index_fragment, suggestionFragment);
                break;
            case MY_SUGGESTIONS:
                MySuggestionsFragment mySuggestionsFragment = new MySuggestionsFragment();
                fragmentTransaction.replace(R.id.index_fragment,mySuggestionsFragment);
                break;
            case ADD_SHOP:
                CrearTiendaFragment crearTiendaFragment = new CrearTiendaFragment();
                fragmentTransaction.replace(R.id.index_fragment,crearTiendaFragment);
                break;
            case MAP:
                MapsFragment mapsFragment = new MapsFragment();
                fragmentTransaction.replace(R.id.index_fragment, mapsFragment);
                break;


        }
        ((FragmentTransaction) fragmentTransaction).addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }


    public void logOut() {
        Toast.makeText(this,"Hasta la proxima  "+mAuth.getCurrentUser().getEmail().toString(),Toast.LENGTH_SHORT).show();
        changeMenu(MenuType.DISCONNECTED);
        setFragment(FragmentName.INDEX);
        mAuth.signOut();
    }

    public GPSManager getGpsManager() {
        return gpsManager;
    }


}