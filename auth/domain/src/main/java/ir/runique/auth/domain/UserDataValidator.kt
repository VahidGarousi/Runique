package ir.runique.auth.domain

import ir.runique.core.domain.util.EmptyDataResult
import ir.runique.core.domain.util.Result

class UserDataValidator constructor(
    private val patternValidator: PatternValidator
) {
    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMenLength = password.length > MINIMUM_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasLowerCaseCharacter = password.any { it.isLowerCase() }
        val hasUpperCaseCharacter = password.any { it.isUpperCase() }
        return PasswordValidationState(
            hasMinimumLength = hasMenLength,
            hasNumber = hasDigit,
            hasLowercaseLetter = hasLowerCaseCharacter,
            hasUppercaseLetter = hasUpperCaseCharacter
        )
    }

    companion object {
        const val MINIMUM_PASSWORD_LENGTH = 9
    }
}
