package com.vitaly_kuznetsov.firebasekotlindemo.data

import android.os.Bundle

class ExceptionBundle(val reason: Reason) : Exception() {

    val extras = Bundle()
    val ERROR_STRING = "ERROR"

    init {
        extras.putString(ERROR_STRING, "Following error occurred: ${reason.name}.")
    }


    // reason of the exception. codes in future can be used
    // for getting the source of exception: api, database
    enum class Reason(private val code: Int) {

        // general exceptions
        UNKNOWN(-1),
        NETWORK_UNAVAILABLE(0),
        EMPTY_FIELDS(1),
        FIREBASE_UNAVAILABLE(2)
    }
}
