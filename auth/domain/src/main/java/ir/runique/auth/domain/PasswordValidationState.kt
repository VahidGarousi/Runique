package ir.runique.auth.domain

data class PasswordValidationState(
    val hasMinimumLength: Boolean = false,
    val hasNumber: Boolean = false,
    val hasLowercaseLetter: Boolean = false,
    val hasUppercaseLetter: Boolean = false,
)
{
    val isValidPassword: Boolean
        get() = hasMinimumLength && hasNumber && hasLowercaseLetter && hasUppercaseLetter
}
