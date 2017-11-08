package com.dev.mendes.android_mytasks.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dev.mendes.android_mytasks.R;
import com.dev.mendes.android_mytasks.fragment.ModTaskFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.dev.mendes.android_mytasks.R.string.titleDialogMapsMarkRemover;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Button btnGetLastLocation;
    TextView textLastLocation;

    private GoogleMap mMap;
    static GoogleApiClient mGoogleApiClient;
    static Location mLastLocation;
    FloatingActionButton fabMeuLocal, fabConcluir;
    MarkerOptions thisMarker;
    Marker myMarker;
    SupportMapFragment mapFragment;

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    static final int RESULT_OK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //btnGetLastLocation = (Button) findViewById(R.id.getlastlocation);
        //btnGetLastLocation.setOnClickListener(btnGetLastLocationOnClickListener);
        //textLastLocation = (TextView) findViewById(R.id.lastlocation);

        // Create an instance of GoogleAPIClient.

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void removeAllMarkers(){
        mMap.clear();
        myMarker = null;
    }

    View.OnClickListener btnGetLastLocationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mGoogleApiClient != null) {
                if (mGoogleApiClient.isConnected()) {
                    getMyLocation();
                } else {
                    Toast.makeText(MapsActivity.this,
                            "!mGoogleApiClient.isConnected()", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MapsActivity.this,
                        "mGoogleApiClient == null", Toast.LENGTH_LONG).show();
            }
        }
    };

    //------------------------------------------------------------------------------
    //ref: Requesting Permissions at Run Time
    //http://developer.android.com/training/permissions/requesting.html
    //------------------------------------------------------------------------------
    public LatLng getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            //------------------------------------------------------------------------------
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return null;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            /*
            textLastLocation.setText(
                    String.valueOf(mLastLocation.getLatitude()) + "\n"
                            + String.valueOf(mLastLocation.getLongitude()));
            */
            Toast.makeText(MapsActivity.this,
                    String.valueOf(mLastLocation.getLatitude()) + "\n"
                            + String.valueOf(mLastLocation.getLongitude()),
                    Toast.LENGTH_LONG).show();
            return new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        } else {
            Toast.makeText(MapsActivity.this,
                    "mLastLocation == null",
                    Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MapsActivity.this,
                            "permission was granted, :)",
                            Toast.LENGTH_LONG).show();
                    getMyLocation();

                } else {
                    Toast.makeText(MapsActivity.this,
                            "permission denied, ...:(",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(MapsActivity.this,
                "onConnectionSuspended: " + String.valueOf(i),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MapsActivity.this,
                "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
    }

    public void controleDeCamera(LatLng latLng){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        mMap.animateCamera(cameraUpdate);
    }

    public void controleDeCamera(LatLng latLng, int zoom){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.animateCamera(cameraUpdate);
    }

    public void ReturnMapData(LatLng arg) {
        Intent intent = this.getIntent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("maps_location", arg);
        intent.putExtra("bundle", bundle);
        this.setResult(this.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        fabMeuLocal = (FloatingActionButton) findViewById(R.id.fabMeuLocal);
        fabConcluir = (FloatingActionButton) findViewById(R.id.fabConcluir);

        fabMeuLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myMarker != null){
                    myMarker.remove();
                }
                LatLng atual = getMyLocation();
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(atual)
                        .title("Meu local")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                );
                controleDeCamera(atual);
            }
        });

        fabConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(thisMarker == null){
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    setResult(ModTaskFragment.RESULT_CANCELED, intent);
                    finish();
                } else {
                    ReturnMapData(new LatLng(thisMarker.getPosition().latitude, thisMarker.getPosition().longitude));
                }
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                removeAllMarkers();
                String lat = String.valueOf(latLng.latitude);
                String lng = String.valueOf(latLng.longitude);
                thisMarker = new MarkerOptions().position(latLng).title(
                        "Marker in Lat = " + lat + " | Lng = " + lng
                ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                mMap.addMarker(thisMarker);
                controleDeCamera(latLng);
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                Toast.makeText(MapsActivity.this,
                        String.valueOf(marker.getId()) + ": " + marker.getTitle(),
                        Toast.LENGTH_LONG).show();

                //Snackbar.make(MapsActivity.this, itleDialogMapsMarkRemover, Snackbar.LENGTH_LONG).show();
                final MaterialDialog.Builder builder = new MaterialDialog.Builder(MapsActivity.this)
                        .title(titleDialogMapsMarkRemover)
                        .positiveText(R.string.agree)
                        .negativeText(R.string.disagree)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                marker.remove();
                            }
                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
                return false;
            }
        });

    }
}