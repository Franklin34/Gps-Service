package cr.ac.gpsservice.service

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import cr.ac.gpsservice.db.LocationDatabase
import cr.ac.gpsservice.entity.Location

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
const val ACTION_FOO = "cr.ac.gpsservice.service.action.FOO"
const val ACTION_BAZ = "cr.ac.gpsservice.service.action.BAZ"

// TODO: Rename parameters
const val EXTRA_PARAM1 = "cr.ac.gpsservice.service.extra.PARAM1"
const val EXTRA_PARAM2 = "cr.ac.gpsservice.service.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions and extra parameters.
 */
class GpsService : IntentService("GpsService") {
    lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationDatabase: LocationDatabase

    companion object{
        val GPS= "cr.ac.servicegps.GPS"
    }

    override fun onHandleIntent(intent: Intent?) {
        locationDatabase = LocationDatabase.getInstance(this)
        getLocation()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(){

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)

        val locationRequest=LocationRequest.create()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationRequest==null) {
                    return
                }
                for (location in locationResult.locations) {
                    val localizacion= Location(null,location.latitude,location.longitude)
                    val bcIntent=Intent()
                    bcIntent.action=GpsService.GPS
                    bcIntent.putExtra("localizacion", localizacion)
                    sendBroadcast(bcIntent)
                    locationDatabase.locationDao.insert(Location(null, localizacion.latitude, localizacion.longitude))
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
        Looper.loop()

    }

}

