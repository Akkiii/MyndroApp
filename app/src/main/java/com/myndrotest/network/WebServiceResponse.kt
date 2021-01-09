package com.myndrotest.network

import com.myndrotest.R
import com.myndrotest.app.MyndroApp
import com.myndrotest.listener.ServiceResponseListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.SocketTimeoutException

class WebServiceResponse {
    private val retrofit: Retrofit = RestClient().retrofitCallBack()
    private val networkCall = retrofit.create(RestClient.NetworkCall::class.java)

    companion object {
        private var instance: WebServiceResponse? = null
        fun getInstance(): WebServiceResponse {
            if (instance == null) {
                instance = WebServiceResponse()
            }
            return instance as WebServiceResponse
        }
    }

    /*Get UserList*/
    @Throws(RuntimeException::class)
    fun getUserList(
        offset: Int,
        limit: Int,
        listener: ServiceResponseListener
    ): Call<AppResponse>? {
        try {
            val res = networkCall.getUserList(offset, limit)
            res.enqueue(object : Callback<AppResponse> {
                override fun onResponse(call: Call<AppResponse>, response: Response<AppResponse>?) {
                    if (response?.body() != null)
                        listener.serviceResponse(
                            response.code(),
                            response.body() as AppResponse,
                            ""
                        )
                    else {
                        listener.serviceResponse(
                            response?.code(),
                            null,
                            response?.errorBody()?.string()
                        )
                    }
                }

                override fun onFailure(call: Call<AppResponse>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        listener.serviceResponse(
                            0, null,
                            MyndroApp.instance.getString(R.string.check_connection)
                        )
                    } else {
                        listener.serviceResponse(0, null, t?.message)
                    }
                }
            })
            return res
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
        return null
    }
}