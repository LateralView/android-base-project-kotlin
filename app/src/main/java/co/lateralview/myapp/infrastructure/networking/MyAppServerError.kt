package co.lateralview.myapp.infrastructure.networking

import com.google.gson.annotations.SerializedName

class MyAppServerError {
    @SerializedName("error_code")
    private val mErrorCode: Int? = null

    @SerializedName("message")
    val errorMessage: String? = null

    val errorCode: Int
        get() = mErrorCode!!
}