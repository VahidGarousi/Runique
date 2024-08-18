package ir.runique.auth.presentation.register

import ir.runique.core.ui.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess : RegisterEvent
    data class Error(
        val error : UiText
    ): RegisterEvent
}