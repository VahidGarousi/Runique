package ir.runique.core.ui

import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration

fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hour = String.format("%02d", totalSeconds / (60 * 60))
    val minute = String.format("%02d", (totalSeconds % 3600) / 60)
    val seconds = String.format("%02d", (totalSeconds % 60) / 60)
    return "$hour:$minute:$seconds"
}


fun Double.toFormattedKm() : String {
    return "${this.roundToDecimalCount(decimalCount = 1)} km"
}

fun Duration.toFormattedPace(distanceInKm : Double) : String {
   if (this == Duration.ZERO || distanceInKm <= 0.0 ) {
       return "-"
   }
    val secondsPerKm = (this.inWholeSeconds / distanceInKm).roundToInt()
    val avgPaceInMinutes = secondsPerKm / 60
    val avgPaceInSec = String.format("%02d",secondsPerKm % 60)
    return "$avgPaceInMinutes:$avgPaceInSec , km"
}


private fun Double.roundToDecimalCount(
    decimalCount : Int
) : Double {
    val factor = 10f.pow(decimalCount)
    return round(this * factor) / factor
}