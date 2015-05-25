package sep.software.anicare.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import sep.software.anicare.R;
import sep.software.anicare.util.AniCareLogger;

/**
 * Created by hongkunyoo on 15. 5. 25..
 */
public class MapActivity extends AniCareActivity implements OnMapReadyCallback{

    public static final int MAP_REQUEST = 1000;
    public static final String RESULT_LONGITUDE = "longitude";
    public static final String RESULT_LATITUDE = "latitude";
    Button pickBtn;
    MarkerOptions mark;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getActionBar().hide();

        pickBtn = (Button)findViewById(R.id.pick_btn);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        initialMap();

        pickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latlng = mark.getPosition();
                mObjectPreference.putClass(latlng);
                Intent result = new Intent();
                result.putExtra(RESULT_LONGITUDE, latlng.longitude);
                result.putExtra(RESULT_LATITUDE, latlng.latitude);
                setResult(RESULT_OK, result);
                finish();
            }
        });

    }

    private void initialMap() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean locationEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!locationEnable) {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return;
        }



//        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                Location myLocation = map.getMyLocation();
//                if (myLocation == null) return false;
//                LatLng myLatLng = new LatLng(myLocation.getLatitude(),
//                        myLocation.getLongitude());
//
//                CameraPosition myPosition = new CameraPosition.Builder()
//                        .target(myLatLng).zoom(17).build();
//                map.animateCamera(
//                        CameraUpdateFactory.newCameraPosition(myPosition));
//                return false;
//            }
//        });
//
//
//        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location myLocation) {
//                if (myLocation == null) return;
//
//                LatLng myLatLng = new LatLng(myLocation.getLatitude(),
//                        myLocation.getLongitude());
//
//                CameraPosition myPosition = new CameraPosition.Builder()
//                        .target(myLatLng).zoom(17).build();
//                map.animateCamera(
//                        CameraUpdateFactory.newCameraPosition(myPosition));
//            }
//        });
//
//
////        CameraPosition myPosition = new CameraPosition.Builder()
////                .target(myLatLng).zoom(17).build();
////        map.animateCamera(
////                CameraUpdateFactory.newCameraPosition(myPosition));
//
//        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            public void onCameraChange(CameraPosition cameraPosition) {
//                map.clear();
//                mark = new MarkerOptions()
//                        .position(cameraPosition.target)
//                        .draggable(true);
//
//                map.addMarker(mark);
//
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initialMap();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        map.setMyLocationEnabled(true);

//        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location myLocation) {
//                if (myLocation == null) return;
//
//                LatLng myLatLng = new LatLng(myLocation.getLatitude(),
//                        myLocation.getLongitude());
//
//                CameraPosition myPosition = new CameraPosition.Builder()
//                        .target(myLatLng).zoom(17).build();
//                map.animateCamera(
//                        CameraUpdateFactory.newCameraPosition(myPosition));
//            }
//        });

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Location myLocation = map.getMyLocation();
                if (myLocation == null) return false;
                LatLng myLatLng = new LatLng(myLocation.getLatitude(),
                        myLocation.getLongitude());

                CameraPosition myPosition = new CameraPosition.Builder()
                        .target(myLatLng).zoom(17).build();
                map.animateCamera(
                        CameraUpdateFactory.newCameraPosition(myPosition));
                return false;
            }
        });

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition cameraPosition) {
                map.clear();
                mark = new MarkerOptions()
                        .position(cameraPosition.target)
                        .draggable(true);

                map.addMarker(mark);
            }
        });

        LatLng savedPoint = mObjectPreference.getClass(LatLng.class);
        LatLng initPoint = new LatLng(37.57280562452983, 126.97690062224865);
        if (savedPoint != null) {
            initPoint = savedPoint;
        }

        CameraPosition initPos = new CameraPosition.Builder()
                .target(initPoint).zoom(14).build();
        map.animateCamera(
                CameraUpdateFactory.newCameraPosition(initPos));

    }
}
