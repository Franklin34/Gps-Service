package cr.ac.gpsservice

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.PolyUtil
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPolygon
import cr.ac.gpsservice.databinding.ActivityMapsBinding
import cr.ac.gpsservice.db.LocationDatabase
import cr.ac.gpsservice.entity.Location
import cr.ac.gpsservice.service.GpsService
import org.json.JSONObject

private lateinit var mMap: GoogleMap
private lateinit var locationDatabase: LocationDatabase
private lateinit var layer : GeoJsonLayer

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var binding: ActivityMapsBinding
    private val SOLICITAR_GPS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationDatabase=LocationDatabase.getInstance(this)
        validaPermisos()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        recuperarPuntos(mMap)
        definePoligono(googleMap)
        iniciaServicio()
    }

    fun recuperarPuntos(googleMap:GoogleMap){
        mMap = googleMap
        for(location in locationDatabase.locationDao.query()){
            val costaRica = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(costaRica).title("Marker in Costa Rica"))
        }

    }

    fun iniciaServicio(){
        val filter= IntentFilter()
        filter.addAction(GpsService.GPS)
        val rcv=ProgressReceiver()
        registerReceiver(rcv,filter)
        startService(Intent(this,GpsService::class.java))
    }

    fun validaPermisos(){
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            // NO TENGO PERMISOS
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                SOLICITAR_GPS
            )
        }
    }


    /**
     * validar que se le dieron los permisos a la app, en caso contrario salir
     */

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            SOLICITAR_GPS -> {
                if ( grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.exit(1)
                }
            }
        }
    }

    fun definePoligono(googleMap: GoogleMap){
        val geoJsonData= JSONObject("{\n" +
                "  \"type\": \"FeatureCollection\",\n" +
                "  \"features\": [\n" +
                "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {},\n" +
                "      \"geometry\": {\n" +
                "        \"type\": \"Polygon\",\n" +
                "        \"coordinates\": [\n" +
                "          [\n" +
                "            [\n" +
                "              -85.62744140625,\n" +
                "              10.595820834654047\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.78125,\n" +
                "              10.163560279490476\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.4296875,\n" +
                "              9.752370139173285\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.0341796875,\n" +
                "              9.60074993224686\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.0341796875,\n" +
                "              10.055402736564236\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.6826171875,\n" +
                "              9.730714305756955\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.26513671875,\n" +
                "              9.405710041600022\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.84765625,\n" +
                "              9.210560107629691\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.78173828125,\n" +
                "              8.646195681181904\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.3203125,\n" +
                "              8.363692651835823\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.47412109375,\n" +
                "              8.689639068127663\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.16650390625,\n" +
                "              8.602747284770018\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.85888671875,\n" +
                "              8.03747339584114\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.12255859375,\n" +
                "              8.385431015567708\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.8369140625,\n" +
                "              8.428904092875392\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.96875,\n" +
                "              8.711358875426512\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.72705078125,\n" +
                "              8.90678000752024\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.90283203125,\n" +
                "              9.123792057073985\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.8369140625,\n" +
                "              9.470735674130932\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.6611328125,\n" +
                "              9.60074993224686\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.47412109375,\n" +
                "              10.293301000109102\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.64990234375,\n" +
                "              10.898042159726009\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.935546875,\n" +
                "              10.746969318460001\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.19921875,\n" +
                "              10.833305983642491\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.44091796875,\n" +
                "              10.984335146101955\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.638671875,\n" +
                "              11.049038346537106\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.92431640625,\n" +
                "              11.005904459659451\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.4296875,\n" +
                "              11.15684527521178\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.62744140625,\n" +
                "              11.199956869621811\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.6494140625,\n" +
                "              10.919617760254697\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.62744140625,\n" +
                "              10.595820834654047\n" +
                "            ]\n" +
                "          ]\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}")

        layer = GeoJsonLayer(googleMap, geoJsonData)
        layer.addLayerToMap()
    }

    /**
     * es la clase para recibir los mensajes de broadcast
     */
    class ProgressReceiver:BroadcastReceiver(){

        fun getPolygon(layer: GeoJsonLayer): GeoJsonPolygon? {
            for (feature in layer.features) {
                return feature.geometry as GeoJsonPolygon
            }
            return null
        }

        /**
         * se obtiene el parametro enviado por el servicio (Location)
         * Coloca en el mapa la localizacion
         * Mueve la camara a esa localizacion
         */
        override fun onReceive(p0: Context, p1: Intent) {
            if(p1.action==GpsService.GPS) {
                val localizacion:Location=
                    p1.getSerializableExtra("localizacion") as Location
                val punto=LatLng(localizacion.latitude,localizacion.longitude)
                mMap.addMarker(MarkerOptions().position(punto).title("Marker"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(punto))

                if(PolyUtil.containsLocation(localizacion.latitude, localizacion.longitude, getPolygon(layer)!!.outerBoundaryCoordinates, false)==false)
                {
                    Toast.makeText(p0,"El punto está fuera",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(p0,"El punto está dentro",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}