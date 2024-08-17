package ir.runique.auth.presentation

sealed interface IntroAction {
    data object OnSignUpClick : IntroAction
    data object OnSignInClick : IntroAction
}