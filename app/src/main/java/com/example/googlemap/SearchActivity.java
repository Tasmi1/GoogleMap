package com.example.googlemap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.googlemap.model.LatitudeLongitude;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<LatitudeLongitude> latitudeLongitudeList;
    Marker markerName;
    CameraUpdate center, zoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //obtain the supportmapfragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);
        
        fillArrayListAndSetAdapter();
        
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etCity.getText().toString()))
                {
                    etCity.setError("Please enter a place name");
                    return;
                    
                }
                
                // get the current location of the place
                
                int position = SearchArrayList(etCity.getText().toString());
                if (position > -1)
                    loadMap(position);
                else
                    Toast.makeText(SearchActivity.this, "Location not found by name : " + etCity.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // This function willl find arraylist with static data and set autocomplete view with marker

    private void fillArrayListAndSetAdapter() {
        latitudeLongitudeList = new ArrayList<>();
        latitudeLongitudeList.add(new LatitudeLongitude(27.7045398, 85.3282075, "Address Restraunt "));
        latitudeLongitudeList.add(new LatitudeLongitude(27.7071881, 85.3273042, "Ichiban Restraunt "));

        latitudeLongitudeList.add(new LatitudeLongitude(27.7076082, 85.3274, "Veda Salon "));
        latitudeLongitudeList.add(new LatitudeLongitude(27.7049619, 85.3291342, "PeopleBot buspark "));

        latitudeLongitudeList.add(new LatitudeLongitude(27.7089893, 85.325198, "Kumari Hall "));
        latitudeLongitudeList.add(new LatitudeLongitude(27.7106308, 85.3193637, "Durbar Marg "));

        String[] data = new String[latitudeLongitudeList.size()];

        for (int i = 0; i < data.length; i++) {
            data[i] = latitudeLongitudeList.get(i).getMarker();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                SearchActivity.this,
                android.R.layout.simple_list_item_1,
                data
        );
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);
    }

        //this fuunction will check weather the location is in list or not

        public int SearchArrayList(String name)
        {
            for (int i = 0; i < latitudeLongitudeList.size(); i++)
            {
                if (latitudeLongitudeList.get(i).getMarker().contains(name))
                {
                    return i;
                }
            }
            return -1;

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        //load kathmandu city when application launches

        mMap = googleMap;
        center = CameraUpdateFactory.newLatLng(new LatLng( 27.7172453, 85.3239605));
        zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    public void loadMap(int position)
    {
        //remove old marker from map

        if(markerName!=null)
        {
            markerName.remove();
        }

        double latitude = latitudeLongitudeList.get(position).getLat();
        double longitude = latitudeLongitudeList.get(position).getLon();
        String marker = latitudeLongitudeList.get(position).getMarker();
        center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        zoom = CameraUpdateFactory.zoomTo(17);
        markerName = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(marker));
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }







}
