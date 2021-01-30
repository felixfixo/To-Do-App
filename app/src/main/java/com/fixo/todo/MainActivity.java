package com.fixo.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fixo.todo.ViewModels.ToDoViewModel;
import com.fixo.todo.ViewModels.ToDoViewModelFactory;
import com.fixo.todo.adapters.TabLayoutAdapter;
import com.fixo.todo.fragments.ActiveToDosFragment;
import com.fixo.todo.fragments.CompletedToDosFragment;
import com.fixo.todo.models.ToDo;
import com.fixo.todo.utils.ConstantValues;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // Member variables
    private ToDoViewModel toDoViewModel;
    public static final int NEW_TO_DO_ACTIVITY_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 22;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView tempTextView, rainfallTextView,speedTextView, kmTextView, fahrenheitTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempTextView = findViewById(R.id.tempTV);
        rainfallTextView = findViewById(R.id.rainfallTV);
        speedTextView = findViewById(R.id.speedTextView);
        kmTextView = findViewById(R.id.kmTextView);
        fahrenheitTextView = findViewById(R.id.fahrenheitTextView);

        // Initialize fused location provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Request location permission from user
        createLocationRequest();
        requestLocationPermission();

        // Call method to get current location of the device
        getLocation();

        // Initialize the view model class
        toDoViewModel = new ViewModelProvider(this, new ToDoViewModelFactory
                (this.getApplication())).get(ToDoViewModel.class);

        // Button opens a new activity where new to-do is created
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewToDoActivity.class);
            startActivityForResult(intent, NEW_TO_DO_ACTIVITY_REQUEST_CODE);
        });

        // Call method to set tabs
        setTabsInfo();


    }

    /**
     * Method that set tabs information
     */
    private void setTabsInfo(){
        // Set the tabs
        TabLayoutAdapter adapter = new TabLayoutAdapter(getSupportFragmentManager(), 1);
        ViewPager viewPager = findViewById(R.id.pager);

        // Add the fragments to the tabs
        adapter.addFragment(new ActiveToDosFragment(toDoViewModel),
                "Active");
        adapter.addFragment(new CompletedToDosFragment(toDoViewModel),
                "Completed");

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TO_DO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the returned date
            String dateInStringFormat = data.getStringExtra(NewToDoActivity.DATE_EXTRA_REPLY);

            // Process the to-do
            ToDo toDo = new ToDo(data.getStringExtra(NewToDoActivity.DESCRIPTION_EXTRA_REPLY),
                    data.getStringExtra(NewToDoActivity.STATUS_EXTRA_REPLY),
                    dateInStringFormat);
            // Save the task to the database
            toDoViewModel.insert(toDo);
        }
    }


    /**
     * Method for tweaking location settings
     */
    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                //requestLocationPermission();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                MY_PERMISSIONS_REQUEST_LOCATION);

                        //Logout user
                        //startActivity(new Intent(GoOnlineActivity.this, LoginActivity.class));
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });


    }

    /**
     * Method for requesting location permission from the user
     */
    public void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Request")
                        .setMessage("We request your permission to access your location." + "\n" +
                                "We will use the location data to check for weather updates")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        // Get location of the device once permission is granted
                        getLocation();
                    }

                }
            }


        }
    }


    /**
     * Method that get device coordinates
     */
    private void getLocation() {
        // Check if location permission has already been granted by the user
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the missing permissions, and then overriding
            requestLocationPermission();
            return;
        }

        // Get the current location coordinates
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            String latitude = String.valueOf(location.getLatitude());
                            String longitude = String.valueOf(location.getLongitude());

                            // Creating Volley string request that gets weather info. from API
                            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                            String url = ConstantValues.WEATHER_BASE_URL + "lat=" + latitude +"&lon=" + longitude +"&appid="+ ConstantValues.API_KEY;

                            // Request a string response from the provided URL.
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    response -> {
                                        Log.d("response", response);
                                        // Call method to process response
                                        processResponse(response);

                                    }, error ->{
                                Log.d("Error Response", error.getMessage());
                                Toast.makeText(MainActivity.this,
                                        R.string.weather_updates_error,
                                        Toast.LENGTH_LONG).show();
                                fahrenheitTextView.setVisibility(View.GONE);
                                kmTextView.setVisibility(View.GONE);
                            }
                            );

                            // Add the request to the RequestQueue.
                            queue.add(stringRequest);


                        }
                        else{
                            Toast.makeText(MainActivity.this,
                                    R.string.location_error_msg, Toast.LENGTH_SHORT).show();
                            fahrenheitTextView.setVisibility(View.GONE);
                            kmTextView.setVisibility(View.GONE);
                        }
                    }
                });
    }


    /**
     * Method that processes the weather API response
     * @param response The String response from the API
     */
    private void processResponse(String response){
        try {

            // Process the response and update the UI
            JSONObject jsonObject = new JSONObject(response);

            // Get Rainfall information
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            int i = 0;
            while( i < jsonArray.length()){
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                rainfallTextView.setText( jsonObject2.optString("description"));
                i++;
            }

            // Get temperature information
            JSONObject jsonObject3 = jsonObject.getJSONObject("main");
            tempTextView.setText(jsonObject3.optString("temp"));

            // Get wind speed information
            JSONObject jsonObject4 = jsonObject.getJSONObject("wind");
            speedTextView.setText(jsonObject4.optString("speed"));

            fahrenheitTextView.setVisibility(View.VISIBLE);
            kmTextView.setVisibility(View.VISIBLE);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}