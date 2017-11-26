package red.padraig.alarmapp.weather

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import red.padraig.alarmapp.SharedPreferencesWrapper

class LocationUpdater(val sharedPrefs: SharedPreferencesWrapper) : LocationListener {
    override fun onLocationChanged(location: Location?) {
        if (location != null) sharedPrefs.setCoordinates(location)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }
}