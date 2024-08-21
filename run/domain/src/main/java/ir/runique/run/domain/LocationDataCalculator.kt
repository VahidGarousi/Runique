package ir.runique.run.domain

import ir.runique.core.domain.location.LocationTimestamp
import kotlin.math.roundToInt

object LocationDataCalculator {
    fun getTotalDistanceInMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations.sumOf { timestampPerLine ->
            timestampPerLine.zipWithNext { location1, location2 ->
                location1.locationWithAltitude.location.distanceTo(location2.locationWithAltitude.location)
            }.sum().roundToInt()
        }
    }
}