package com.myndrotest.listener

interface ServiceResponseListener {
    fun serviceResponse(resStatus: Int?, objectRes: Any?, errorMessage: String?)
}