package ru.kotlin.homework.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.kotlin.homework.Circle

class NetworkResponseTest {

    @Test
    fun success_createNetworkResponse_success() {
        val response = Success("Message")
        assertTrue(response is NetworkResponse<*, *>)
        assertTrue(response is Success)
        assertEquals("Message", response.resp)
    }

    @Test
    fun failure_createNetworkResponse_failure() {
        val error = Error()
        val response = Failure(error)
        assertTrue(response is NetworkResponse<*, *>)
        assertTrue(response is Failure)
        assertEquals(error, response.error)
    }

    @Test
    fun success_assignToNetworkResponse_success() {
        val success = Success("Message")
        val response1: NetworkResponse<String, Error> = success
        val response2: NetworkResponse<Any, Error> = success
        assertTrue(response1 is Success)
        assertEquals("Message", (response1 as Success).resp)
        assertTrue(response2 is Success)
        assertEquals("Message", (response2 as Success).resp)
    }

    @Test
    fun success_assignToNetworkResponse_exception() {
        val success = Success("Message")
        val response1: NetworkResponse<String, Exception> = success
        val response2: NetworkResponse<Any, Exception> = success
        assertTrue(response1 is Success)
        assertEquals("Message", (response1 as Success).resp)
        assertTrue(response2 is Success)
        assertEquals("Message", (response2 as Success).resp)
    }

    @Test
    fun success_assignToSuccess_success() {
        val success = Success(String())
        val response1: Success<CharSequence> = success
        val response2: Success<Any> = success
        assertEquals(success, response1)
        assertEquals(success, response2)
    }

    @Test
    fun failure_assignToNetworkResponse_failure() {
        val failure = Failure(Error())
        val response1: NetworkResponse<String, Error> = failure
        val response2: NetworkResponse<Any, Error> = failure
        assertTrue(response1 is Failure)
        assertEquals(failure.error, (response1 as Failure).error)
        assertTrue(response2 is Failure)
        assertEquals(failure.error, (response2 as Failure).error)
    }

    @Test
    fun failure_assignToNetworkResponse_throwable() {
        val failure = Failure(Error())
        val response1: NetworkResponse<Any, Throwable> = failure
        assertTrue(response1 is Failure)
        assertEquals(failure.error, (response1 as Failure).error)
    }

    @Test
    fun failure_assignToNetworkResponse_int() {
        val failure = Failure(ApiException.NotAuthorized)
        val response: NetworkResponse<Int, Throwable> = failure
        assertTrue(response is Failure)
        assertEquals(failure.error, (response as Failure).error)
    }

    @Test
    fun failure_errorMessage_getErrorMessage() {
        val error = Error()
        val failure = Failure(error)
        val message = failure.error.message
        assertEquals(error.message, message)
    }

    @Test
    fun errorLogger_logThrowable_success() {
        val logger = ErrorLogger<Throwable>()
        val response1 = Success("Success")
        val response2 = Success(Circle)
        val response3 = Failure(IllegalArgumentException("Something unexpected"))

        logger.log(response1)
        logger.log(response2)
        logger.log(response3)

        val dump = logger.dump()
        assertEquals(1, dump.size)
        assertEquals(response3.responseDateTime, dump[0].first)
        assertEquals(response3.error, dump[0].second)
    }

    @Test
    fun errorLogger_logApiException_success() {
        val logger = ErrorLogger<ApiException>()
        val response1 = Success("Success")
        val response2 = Success(Circle)
        val response3 = Failure(ApiException.NetworkException)
        val response4 = Failure(ApiException.UnknownException)

        logger.log(response1)
        logger.log(response2)
        logger.log(response3)
        logger.log(response4)

        val dump = logger.dump()
        assertEquals(2, dump.size)
        assertEquals(response3.responseDateTime, dump[0].first)
        assertEquals(response3.error, dump[0].second)
        assertEquals(response4.responseDateTime, dump[1].first)
        assertEquals(response4.error, dump[1].second)
    }
}
