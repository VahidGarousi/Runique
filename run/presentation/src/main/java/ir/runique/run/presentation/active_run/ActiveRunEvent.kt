package ir.runique.run.presentation.active_run

import ir.runique.core.ui.UiText

sealed interface ActiveRunEvent {
    data class Error(val error : UiText) : ActiveRunEvent
    data object RunSaved : ActiveRunEvent
}