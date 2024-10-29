package ru.kotlin

import java.io.IOException

fun main() {
    val loginError1 = LoginException.Network("Timeout", false)
    val loginError2 = LoginException.Auth("Wrong username or password")
    val regError1 = RegException.Conflict("Username already taken")

    logError(loginError1)
    logError(loginError2)
    logError(regError1)
}

/**
 * Критичность ошибки
 */
interface WithFatality {
    val isFatal: Boolean
}

/**
 * Набор ошибок для процедуры логина
 */
sealed interface LoginException : WithFatality {
    class Network(message: String, override val isFatal: Boolean) : IOException(message), LoginException
    class Auth(message: String) : SecurityException(message), LoginException {
        override val isFatal: Boolean = true
    }
}

/**
 * Набор ошибок для процедуры регистрации
 */
sealed interface RegException : WithFatality {
    class Network(message: String, override val isFatal: Boolean) : IOException(message), RegException
    class Conflict(message: String) : IllegalArgumentException(message), RegException {
        override val isFatal: Boolean = true
    }
}

fun <E> logError(error: E) where E: Throwable, E: WithFatality {
    // Свойство из Throwable
    println("Message: ${error.message}")
    // Свойство из WithFatality
    println("Fatal: ${error.isFatal}")
}