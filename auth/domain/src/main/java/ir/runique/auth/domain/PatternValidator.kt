package ir.runique.auth.domain

interface PatternValidator {
    fun matches(value : String) : Boolean
}