package co.lateralview.myapp.infrastructure.networking

object RestConstants {
    //    val BASE_URL = BuildConfig.BASE_URL
    val BASE_URL = "https://api.github.com/"
    // TODO Set Auth Header
    val HEADER_AUTH = "auth-header"

    enum class Subcode private constructor(val subcode: Int) {
        INVALID_TOKEN(200002);

        companion object {

            fun fromInt(code: Int): Subcode? {
                for (subcode in Subcode.values()) {
                    if (subcode.subcode == code) {
                        return subcode
                    }
                }
                return null
            }
        }
    }
}
