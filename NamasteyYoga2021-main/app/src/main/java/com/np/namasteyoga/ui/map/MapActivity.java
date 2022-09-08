package com.np.namasteyoga.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.np.namasteyoga.BuildConfig;
import com.np.namasteyoga.R;
import com.np.namasteyoga.utils.C;
import com.np.namasteyoga.utils.ConstUtility;
import com.np.namasteyoga.utils.IntentUtils;
import com.np.namasteyoga.utils.Logger;

import java.text.DecimalFormat;

import im.delight.android.location.SimpleLocation;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapClickListener, OnLocaleChangedListener {
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;
    private LatLng latLng = null;

    private LocalizationActivityDelegate localizationDelegate =  new LocalizationActivityDelegate(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (getIntent() != null && getIntent().hasExtra(IntentUtils.key)) {
            latLng = getIntent().getParcelableExtra(IntentUtils.key);
        }
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

        findViewById(R.id.ivBack).setOnClickListener(v -> done());
        findViewById(R.id.ivDone).setOnClickListener(v -> done());
    }

    private void done() {
        Intent intent = new Intent();
        intent.putExtra(IntentUtils.key, latLng);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private static final String TAG = "MapActivity";

    @Override
    protected void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
        runnable = () -> {
            if (isLocationPermissionGranted()) {
                if (ConstUtility.isGpsEnabled(MapActivity.this)) {
                    Logger.INSTANCE.Debug("Location Enable", TAG);
                    if (googleMap!=null){
                        onMapReady(googleMap);
                    }

                } else {
                    ConstUtility.showSettingsAlert(MapActivity.this);
                }
            } else {
                requestPermissionForLocation();
            }
        };

        if (!isLocationPermissionGranted() || !ConstUtility.isGpsEnabled(MapActivity.this))
            handler.postDelayed(runnable, C.SPLASH_LOADER_TIME);
    }

    private void requestPermissionForLocation() {
        try {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
            ) {
                getDialogConfirm(getString(R.string.location_permission), "");

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLocationPermissionGranted() {

        try {

            if (Build.VERSION.SDK_INT >= 23)
                return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        ;
            else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    void getDialogConfirm(String dataText, String titleText) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //tell the Dialog to use the dialog.xml as it's layout description
            dialog.setContentView(R.layout.dialog_with_two_button);
            // dialog.setTitle("Android Custom Dialog Box");
            dialog.setCancelable(false);
            TextView dataTextTv = (TextView) dialog.findViewById(R.id.dialog_data_text);
            TextView titleTextTv = (TextView) dialog.findViewById(R.id.dialog_title_text);
            TextView cancelTv = (TextView) dialog.findViewById(R.id.dialog_cancel_text);
            TextView okTextTv = (TextView) dialog.findViewById(R.id.dialog_ok_text);

            cancelTv.setVisibility(View.GONE);
            dataTextTv.setText(dataText);
            titleTextTv.setText(titleText);

            cancelTv.setOnClickListener(v -> dialog.dismiss());

            okTextTv.setOnClickListener(v -> {
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + C.APPLICATION_ID)));
                dialog.dismiss();
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GoogleMap googleMap;

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MapActivity.this.latLng = latLng;
                mapWork();
            }
        });
        if (latLng!=null){
            mapWork();
            return;
        }
        if (isLocationPermissionGranted() && ConstUtility.isGpsEnabled(MapActivity.this)) {
//            googleMap.setMyLocationEnabled(false);
            setPinOnMap();
        }

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latLng = marker.getPosition();
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        marker.remove();
        markerOptions.title(getMapTitle()).position(latLng);
        googleMap.addMarker(markerOptions);
    }

    private MarkerOptions markerOptions;

    @SuppressLint("MissingPermission")
    private void setPinOnMap() {
//        Location location = this.googleMap.getMyLocation();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }


        if (myLocation != null) {

            LatLng target = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

            if (latLng != null) {
                target = latLng;
            } else {
                latLng = target;
            }
            DecimalFormat df = new DecimalFormat(".######");
            latLng = new LatLng(Double.parseDouble(df.format(latLng.latitude)),Double.parseDouble(df.format(latLng.longitude)));
            mapWork();
        } else {
            SimpleLocation simpleLocation = new SimpleLocation(this);

            LatLng target = new LatLng(simpleLocation.getLatitude(), simpleLocation.getLongitude());

            if (latLng != null) {
                target = latLng;
            } else {
                latLng = target;
            }

            if (latLng != null) {
                DecimalFormat df = new DecimalFormat(".######");
                latLng = new LatLng(Double.parseDouble(df.format(latLng.latitude)),Double.parseDouble(df.format(latLng.longitude)));
                mapWork();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void mapWork() {
        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.zoom(15);
        builder.target(latLng);
        googleMap.setMyLocationEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)).draggable(true)
                .title(getMapTitle());
        googleMap.addMarker(markerOptions);
    }

    private String getMapTitle() {
        return latLng == null ? "" : "" + latLng.latitude + "," + latLng.longitude;
    }



    @Override
    public void onMapClick(LatLng latLng) {
        if (this.latLng == null) {
            this.latLng = latLng;
            DecimalFormat df = new DecimalFormat(".######");
            this.latLng = new LatLng(Double.parseDouble(df.format(latLng.latitude)),Double.parseDouble(df.format(latLng.longitude)));
            mapWork();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (isLocationPermissionGranted()) {
                setPinOnMap();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase));
        super.attachBaseContext(newBase);
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    @Override
    public void onAfterLocaleChanged() {

    }

    @Override
    public void onBeforeLocaleChanged() {

    }
}