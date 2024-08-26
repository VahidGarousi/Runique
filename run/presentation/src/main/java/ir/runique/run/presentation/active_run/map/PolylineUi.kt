package ir.runique.run.presentation.active_run.map

import androidx.compose.ui.graphics.Color
import ir.runique.core.domain.location.Location

data class PolylineUi(
    val location1: Location,
    val location2: Location,
    val color: Color
)
