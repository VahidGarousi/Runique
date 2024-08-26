package ir.runique.run.presentation.run_overview.mappers

import ir.runique.core.domain.run.Run
import ir.runique.core.ui.formatted
import ir.runique.core.ui.toFormattedKm
import ir.runique.core.ui.toFormattedKmh
import ir.runique.core.ui.toFormattedMeters
import ir.runique.core.ui.toFormattedPace
import ir.runique.run.presentation.run_overview.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {
    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - HH:mma")
        .format(dateTimeInLocalTime)
    val distanceKm = distanceInMeters / 1000.0
    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl
    )
}