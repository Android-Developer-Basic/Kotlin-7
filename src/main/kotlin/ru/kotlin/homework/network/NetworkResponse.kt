@file:Suppress("unused")

package ru.kotlin.homework.network

import java.lang.IllegalArgumentException
import java.time.LocalDateTime

/**
 * Network result
 */
sealed class NetworkResponse<T, R> {
    val responseDateTime: LocalDateTime = LocalDateTime.now()
}

/**
 * Network success
 */
data class Success<T>(val resp: T) : NetworkResponse<T, Nothing>()

/**
 * Network error
 */
data class Failure<R>(val error: R) : NetworkResponse<Nothing, R>()

val s1 = Success("Message")

val r11: Success<String> = s1
val r12: Success<String> = s1

val s2 = Success("Message")
val r21: Success<String> = s2
val r22: Success<String> = s2

val s3 = Success(String())
val r31: Success<String> = s3
val r32: Success<String> = s3

val e = Failure(Error())
val er1: Failure<Error> = e
val er2: Failure<Error> = e
val er4: Failure<Error> = e

val er5: Failure<IllegalArgumentException> = Failure(IllegalArgumentException("message"))

val message = e.error.message