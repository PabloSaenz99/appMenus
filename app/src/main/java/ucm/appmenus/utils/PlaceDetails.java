package ucm.appmenus.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import ucm.appmenus.Restaurante;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

public class PlaceDetails {

    private Context context;
    private PlacesClient placesClient;
    private Restaurante restaurante = null;
    private ArrayList<Restaurante> listaRestaurantes;

    public PlaceDetails(Context appContext){
        this.context = appContext;
        placesClient = Places.createClient(context);
        listaRestaurantes = new ArrayList<Restaurante>();
        // Initialize the SDK
        //TODO: Hay que poner esto en alguna activity:
        Places.initialize(context, "apiKey");
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(context);
        //TODO: Hay que hacer lo de la key:
        //https://developers.google.com/maps/documentation/places/android-sdk/start?hl=es
    }

    //Codigo de: https://developers.google.com/maps/documentation/places/android-sdk/place-details?hl=es
    public Restaurante getBasicPlaceData(String placeId) {
        // Specify the fields to return.
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.WEBSITE_URI, Place.Field.ICON_URL);
        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse response) {
                Place place = response.getPlace();
                restaurante =  new Restaurante(place.getId(), place.getName(), place.getWebsiteUri().toString(),
                        place.getRating(), place.getIconUrl(), null, null);
                Log.i(TAG, "Place found: " + place.getName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();
                    // TODO: Handle error with given status code.
                }
            }
        });
        return restaurante;
    }

    public List<Restaurante> getCurrentPlaces(){
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.ID);
        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()) {
                        FindCurrentPlaceResponse response = task.getResult();
                        /**
                         * Devuelve una lista de sitios
                         * */
                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                            Place place = placeLikelihood.getPlace();
                            Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                    place.getName(),
                                    placeLikelihood.getLikelihood()));
                            listaRestaurantes.add(new Restaurante(place.getId(), place.getName(),
                                    place.getWebsiteUri().toString(), place.getRating(), place.getIconUrl(),
                                    null, null));
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                }
            });
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            //TODO: getLocationPermission();
            //getLocationPermission();
        }
        return Collections.unmodifiableList(listaRestaurantes);
    }

    public String refreshPlaceID(String placeId){
        final String[] nuevoId = new String[0];
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID);

        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse response) {
                nuevoId[0] = response.getPlace().getId();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof ApiException) {
                    nuevoId[0] = null;
                }
            }
        });
        return nuevoId[0];
    }
}
