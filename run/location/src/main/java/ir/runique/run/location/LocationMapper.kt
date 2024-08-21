package ir.runique.run.location

import android.location.Location
import ir.runique.core.domain.location.LocationWithAltitude

fun Location.toLocationWithAltitude() : LocationWithAltitude {
    return LocationWithAltitude(
        location = ir.runique.core.domain.location.Location(
            lat = latitude,
            long = longitude
        ),
        altitude = altitude
    )
}