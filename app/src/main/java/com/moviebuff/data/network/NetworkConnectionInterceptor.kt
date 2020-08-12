package com.moviebuff.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.moviebuff.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkConnectionInterceptor() : Interceptor{


    override fun intercept(chain: Interceptor.Chain): Response {

        try{
            //Add Header
            val request = chain.request()


            val newRequest : Request
                newRequest = request.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build()
            AppLogger.logRequest(newRequest)

            val response = chain.proceed(newRequest)
            val bodyString =
                response.peekBody(Long.MAX_VALUE).string()
            AppLogger.logResponse(newRequest.url.toString(), response.code, bodyString)


            return response


        }catch ( e: ConnectException){
            throw NoInternetException("Error Connecting to internet")
        }catch ( e: SocketTimeoutException){
            throw NoInternetException("Error Connecting to internet")
        }catch (e : UnknownHostException){
            throw NoInternetException("Unknown Host")
        }
    }
}