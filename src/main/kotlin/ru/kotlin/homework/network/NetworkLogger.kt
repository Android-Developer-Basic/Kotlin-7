@file:Suppress("unused")

package ru.kotlin.homework.network

import ru.kotlin.homework.Circle
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

/**
 * Известный вам список ошибок
 */
sealed class ApiException(message: String) : Throwable(message) {
    data object NotAuthorized : ApiException("Not authorized") {
        private fun readResolve(): Any = NotAuthorized
    }

    data object NetworkException : ApiException("Not connected") {
        private fun readResolve(): Any = NetworkException
    }

    data object UnknownException : ApiException("Unknown exception") {
        private fun readResolve(): Any = UnknownException
    }
}

class ErrorLogger<E : Throwable> {

    private val errors = mutableListOf<Pair<LocalDateTime, *>>()

    fun log(response: NetworkResponse<*, *>) {
        if (response is Failure) {
            errors.add(response.responseDateTime to response.error)
        }
    }

    fun dump(): MutableList<Pair<LocalDateTime, *>> {
        return errors
    }

    fun dumpLog() {
        errors.forEach { (date, error) ->
            println("Error at $date: $error")
        }
    }
}

fun processThrowables(logger: ErrorLogger<Throwable>) {

    logger.log(Success("Success"))
    Thread.sleep(100)
    logger.log(Success(Circle))
    Thread.sleep(100)
    logger.log(Failure(IllegalArgumentException("Something unexpected")))

    logger.dumpLog()
}

fun processApiErrors(apiExceptionLogger: ErrorLogger<*>) {
    apiExceptionLogger.log(Success("Success"))
    Thread.sleep(100)
    apiExceptionLogger.log(Success(Circle))
    Thread.sleep(100)

    val api = ApiException.NetworkException
    val tt = Failure(api)


    apiExceptionLogger.log(tt)

    apiExceptionLogger.dumpLog()
}

fun main() {
    val logger = ErrorLogger<Throwable>()


    println("Processing Throwable:")
    processThrowables(logger)

    println("Processing Api:")
    processApiErrors(logger)

    val errors = logger.dump()
    errors.forEach {
        println("List Errors = $it")
    }
}

