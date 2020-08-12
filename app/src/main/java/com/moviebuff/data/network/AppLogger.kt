package com.moviebuff.data.network

import android.util.Log
import com.moviebuff.BuildConfig
import okhttp3.Headers
import okhttp3.Request
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

object AppLogger {
    private const val NETWORK_TAG = "NETWORK"
    private fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }

    fun logRequest(request: Request) {
        if (BuildConfig.DEBUG) {
            try {
                val sb = StringBuilder()
                sb.append("==============================================================================\n")
                sb.append("--> METHOD: ${AppLogger.getMethod(request.method)}")
                sb.append("--> URL: ${request.url}")
                sb.append("--> HEADERS: ${AppLogger.getHeaders(request.headers)}")
                if (request.body != null && request.body.toString().length > 2) {
                    val data: String = AppLogger.bodyToString(request)
                    try {
                        val jsonObject = JSONObject(data)
                        sb.append("--> BODY:\n")
                        var dataString = jsonObject.toString(4)
                        dataString = dataString.replace("\\/", "/")
                        sb.append(dataString)
                        sb.append("\n")
                    } catch (e: JSONException) {
                        try {
                            val jsonArray = JSONArray(data)
                            sb.append("--> BODY:\n")
                            var dataString = jsonArray.toString(4)
                            dataString = dataString.replace("\\/", "/")
                            sb.append(dataString)
                            sb.append("\n")
                        } catch (e2: JSONException) {
                            sb.append("--> BODY:\n")
                            val arr = data.split("&").toTypedArray()
                            var i = 0
                            while (i < arr.size) {
                                sb.append(arr[i])
                                sb.append("\n")
                                i++
                            }
                        }
                    }
                } else {
                    sb.append("--> BODY: <empty>\n")
                }
                sb.append("==============================================================================\n")
                AppLogger.logNetwork(sb.toString())
            } catch (e: Exception) {
                AppLogger.logNetwork("logRequest error: $e")
            }
        }
    }

    fun getMethod(method: String): String {
        return method
    }

    fun getHeaders(map: Headers): String {
        return map.toString()
    }

    fun logResponse(url: String, httpCode: Int, data: String) {
        if (BuildConfig.DEBUG) {
            try {
                val sb = StringBuilder()
                sb.append("==============================================================================\n")
                sb.append("<-- URL: $url\n")
                sb.append("<-- STATUS CODE: $httpCode\n")
                try {
                    val jsonObject = JSONObject(data)
                    sb.append("<-- DATA:\n")
                    sb.append("==============================================================================\n")
                    var dataString = jsonObject.toString(4)
                    dataString = dataString.replace("\\/", "/")
                    sb.append(dataString)
                    sb.append("\n")
                    sb.append("==============================================================================\n")
                } catch (e: JSONException) {
                    try {
                        val jsonArray = JSONArray(data)
                        sb.append("<-- DATA:\n")
                        sb.append("==============================================================================\n")
                        sb.append(jsonArray.toString())
                        sb.append("==============================================================================\n")
                        var dataString = jsonArray.toString(4)
                        dataString = dataString.replace("\\/", "/")
                        sb.append(dataString)
                        sb.append("\n")
                        sb.append("==============================================================================\n")
                    } catch (e2: JSONException) {
                        sb.append("<-- DATA: $data\n")
                    }
                }
                sb.append("==============================================================================\n")
                AppLogger.logNetwork(sb.toString())
            } catch (e: Exception) {
                AppLogger.logNetwork("logResponse error: $e")
            }
        }
    }

    private fun logNetwork(msg: String) {
        Log.d(AppLogger.NETWORK_TAG, msg)
    }
}
