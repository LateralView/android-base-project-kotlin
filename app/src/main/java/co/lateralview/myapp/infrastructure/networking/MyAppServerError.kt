package co.lateralview.myapp.infrastructure.networking

import com.google.gson.annotations.SerializedName

class MyAppServerError {
    @SerializedName("error_code")
    val mErrorCode: Int? = null

    @SerializedName("message")
    val mErrorMessage: String? = null
}