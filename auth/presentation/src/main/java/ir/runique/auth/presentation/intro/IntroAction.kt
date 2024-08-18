package ir.runique.auth.presentation.intro

sealed interface IntroAction {
    data object OnSignUpClick : IntroAction
    data object OnSignInClick : IntroAction
}