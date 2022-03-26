package ucm.appmenus.ui.filtros;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.Toast;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;

import java.io.File;
import java.io.InputStream;

import ucm.appmenus.R;
import ucm.appmenus.entities.Usuario;

//https://github.com/mapsforge/mapsforge/blob/master/docs/Mapsforge-Maps.md mapas??
public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private static final String MAP_FILE = "world.map";

    private double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_map);
        //TODO activar esto
        lat = 52.517037;    //Usuario.getUsuario().getLocalizacion().latitude;
        lon = 13.38886;     //Usuario.getUsuario().getLocalizacion().longitude;

        AndroidGraphicFactory.createInstance(getApplication());

        /*
         * A MapView is an Android View (or ViewGroup) that displays a mapsforge map. You can have
         * multiple MapViews in your app or even a single Activity. Have a look at the mapviewer.xml
         * on how to create a MapView using the Android XML Layout definitions. Here we create a
         * MapView on the fly and make the content view of the activity the MapView. This means
         * that no other elements make up the content of this activity.
         */
        mapView = new MapView(this);
        setContentView(mapView);


        try {
            /*
             * We then make some simple adjustments, such as showing a scale bar and zoom controls.
             */
            mapView.setClickable(true);
            mapView.getMapScaleBar().setVisible(true);
            mapView.setBuiltInZoomControls(true);
            mapView.setZoomLevelMax((byte) 15);
            mapView.setZoomLevelMin((byte) 6);

            //Para poner un marcador en la posicion actual del usuario      //https://stackoverflow.com/questions/50445633/why-not-display-any-marker-in-android-mapsforge-map
            Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.logo));
            bitmap.incrementRefCount();
            Marker marker = new Marker(new LatLong(lat, lon), bitmap, 0, -bitmap.getHeight() / 2) {
                @Override public boolean onTap(LatLong geoPoint, Point viewPosition, Point tapPoint) {
                    if (contains(viewPosition, tapPoint)) {
                        Toast.makeText(MapActivity.this, "Urmia, payamasli", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }
            };
            mapView.getLayerManager().getLayers().add(marker);

            /*
             * To avoid redrawing all the tiles all the time, we need to set up a tile cache with an
             * utility method.
             */
            TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                    mapView.getModel().displayModel.getTileSize(), 1f,
                    mapView.getModel().frameBufferModel.getOverdrawFactor());

            /*
             * Now we need to set up the process of displaying a map. A map can have several layers,
             * stacked on top of each other. A layer can be a map or some visual elements, such as
             * markers. Here we only show a map based on a mapsforge map file. For this we need a
             * TileRendererLayer. A TileRendererLayer needs a TileCache to hold the generated map
             * tiles, a map file from which the tiles are generated and Rendertheme that defines the
             * appearance of the map.
             */
            MapDataStore mapDataStore = new MapFile(new File(getFilesDir(), MAP_FILE));
            TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);

            /*
             * On its own a tileRendererLayer does not know where to display the map, so we need to
             * associate it with our mapView.
             */
            mapView.getLayerManager().getLayers().add(tileRendererLayer);

            /*
             * The map also needs to know which area to display and at what zoom level.
             * Note: this map position is specific to Berlin area.
             */
            mapView.setCenter(new LatLong(lat, lon));
            mapView.setZoomLevel((byte) 12);
        } catch (Exception e) {
            /*
             * In case of map file errors avoid crash, but developers should handle these cases!
             */
            e.printStackTrace();
        }
    }
}