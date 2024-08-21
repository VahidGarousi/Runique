@file:OptIn(ExperimentalCoroutinesApi::class)

package ir.runique.run.domain

import ir.runique.core.domain.location.LocationTimestamp
import ir.runique.core.domain.location.LocationWithAltitude
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {
    private val _runData = MutableStateFlow(RunData())
    val runData = _runData.asStateFlow()

    private val isTracking = MutableStateFlow(false)
    private val isObservingLocation: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _elapsedTime: MutableStateFlow<Duration> = MutableStateFlow(Duration.ZERO)
    val elapsedTime: StateFlow<Duration> = _elapsedTime.asStateFlow()


    val currentLocation: StateFlow<LocationWithAltitude?> = isObservingLocation
        .flatMapLatest { isObservingLocation ->
            if (isObservingLocation) {
                locationObserver.observeLocation(1000L)
            } else {
                flowOf()
            }
        }
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )


    init {
        isTracking
            .flatMapLatest { isTracking ->
                if (isTracking) {
                    Timer.timeAndEmit()
                } else {
                    flowOf()
                }
            }
            .onEach {
                _elapsedTime.value = it
            }
            .launchIn(applicationScope)


        currentLocation
            .filterNotNull()
            .combineTransform(isTracking) { location, isTracking ->
                if (isTracking) {
                    emit(location)
                }
            }
            .zip(_elapsedTime) { location, elapsedTime ->
                LocationTimestamp(
                    locationWithAltitude = location,
                    durationTimestamp = elapsedTime
                )
            }
            .onEach { location ->
                val currentLocationsList = runData.value.locations
                val lasLocationsList = if (currentLocationsList.isNotEmpty()) {
                    currentLocationsList.last() + location
                } else {
                    listOf(location)
                }
                val newLocationsList = currentLocationsList.replaceLast(lasLocationsList)

                val distanceMeters = LocationDataCalculator.getTotalDistanceInMeters(
                    locations = newLocationsList
                )
                val distanceKm = distanceMeters / 1000.0
                val currentDuration = location.durationTimestamp
                val averageSecondsPerKm = if (distanceKm == 0.0) 0 else {
                    (currentDuration.inWholeSeconds / distanceKm).roundToInt()
                }

                _runData.update {
                    RunData(
                        distanceMeters =distanceMeters,
                        pace = averageSecondsPerKm.seconds,
                        locations = newLocationsList
                    )
                }
            }
            .launchIn(applicationScope)
    }


    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }

    fun saveIsTracking(isTracking: Boolean) {
        this.isTracking.value = isTracking
    }
}


private fun <T> List<List<T>>.replaceLast(replacement: List<T>): List<List<T>> {
    if (this.isEmpty()) {
        return listOf(replacement)
    }
    return this.dropLast(1) + listOf(replacement)
}