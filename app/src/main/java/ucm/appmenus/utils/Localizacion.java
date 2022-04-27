package ucm.appmenus.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import ucm.appmenus.MainActivity;

/*
Paginas de utilidad:
https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
https://stackoverflow.com/questions/10524381/gps-android-get-positioning-only-once/10524443#10524443 (respuesta 40)
 */

public class Localizacion {

    private final Context context;
    private final MainActivity mainActivity;
    private final int PERMISSION_ID = 44;

    private FusedLocationProviderClient fusedLocationClient;

    public double longitude;
    public double latitude;

    public Localizacion(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.context = this.mainActivity.getApplicationContext();
        actualizarLocalizacion();
    }

    @SuppressLint("MissingPermission")
    public void actualizarLocalizacion(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                final LocationManager lm = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null) {
                    //TODO: descomentar
                    longitude = -122.0965848;//location.getLongitude();
                    latitude = 37.3943617;//location.getLatitude();
                    Log.d("Localizacion", longitude + " and " + latitude);
                }
                else{
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            lm.removeUpdates(this);
                            Log.d("Location Changed", longitude + " and " + latitude);
                        }
                    });
                }
            } else {
                Toast.makeText(context, "Por favor permitenos utilizar la localizacion", Toast.LENGTH_LONG).show();
                //Va mal en android 10
                //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //mainActivity.startActivity(intent);
            }
        } else {
            // if permissions aren't available, request for permissions
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(mainActivity, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location mLastLocation = locationResult.getLastLocation();
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }
        };

        // setting LocationRequest on FusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    /**
     * Calcula la distancia en metros entre 2 coordenadas.
     * @param lat1 latitud de la primera coordenada.
     * @param lon1 longitud de la primera coordenada.
     * @param lat2 latitud de la segunda coordenada.
     * @param lon2 longitud de la segunda coordenada.
     * @return la distancia en metros entre ambas coordenadas.
     */
    public static int distanciaEnMetros(double lat1, double lon1, double lat2, double lon2){
        double dlat = (Math.toRadians(lat2) - Math.toRadians(lat1));
        double dlon = (Math.toRadians(lon2) - Math.toRadians(lon1));
        double sinlat = Math.sin(dlat / 2);
        double sinlon = Math.sin(dlon / 2);

        double a = (sinlat * sinlat) + Math.cos(lat1)*Math.cos(lat2)*(sinlon*sinlon);
        double c = 2 * Math.asin (Math.min(1.0, Math.sqrt(a)));

        return (int) (6371 * c * 1000);
    }
    /*
    private void init(){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(!isLocationEnabled(locationManager)) {
            showAlert();
        }

        ActivityCompat.requestPermissions(mainActivity, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        try {
            //LocationManager.GPS_PROVIDER
            LocationServices.getFusedLocationProviderClient(context).getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    System.out.println("Entreeeeeeeeeeeeeeeeeeeeesadgnajndgsa---------sf,ñsdf-.saf,asi");
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Cagaste weyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                }
            })
            .addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    System.out.println("Canceladiiiisimoooooooooooooooooooooooooo");
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    System.out.println("Completadoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                }
            });
            //network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else {
            latitude = 0.0;
            longitude = 0.0;
        }


        System.out.println("Localizacion: " + longitude + ", " + latitude);

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                userCountry = addresses.get(0).getCountryName();
                userAddress = addresses.get(0).getAddressLine(0);
                //tv.setText(userCountry + ", " + userAddress);
                for (Address a: addresses)
                    System.out.println("-----------------Localizacion-----------------\n"
                            + userCountry + ", " + userAddress);
            }
            else {
                System.out.println("-----------------No hay localizacion-----------------");
                userCountry = "Unknown";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isLocationEnabled(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Ubicaicón")
            .setMessage("La ubicación esta desactivada.\nPor favor activa la ubicación")
            .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
        dialog.show();
    }
    */
}
