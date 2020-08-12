package com.moviebuff.data.network

import com.moviebuff.utils.ApiException
import com.moviebuff.utils.NoResponseBodyException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.NullPointerException
import java.net.UnknownHostException

abstract class SafeApiRequest{
    suspend fun<T:Any> apiRequset(call : suspend () ->Response<T>) :T{
        val response :Response<T>
        try{
            response = call.invoke()

            try{
                if(response.isSuccessful){
                    return response.body()!!
                }else{
                    val error = response.errorBody()?.string()
                    var message = String()
                    error?.let {
                        message = try{
                            JSONObject(it).getJSONObject("errors").getString("message")
                        }catch (e : JSONException){
                            "Something went Wrong"
                        }
                    }
                    throw ApiException(message)
                }
            }catch (e : NullPointerException){
                throw NoResponseBodyException(response.headers()["X-Items-Count"])
            }
        }catch (e : UnknownHostException){
            throw ApiException("Error Connecting to Internet")
        }
    }
}