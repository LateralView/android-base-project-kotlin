package co.lateralview.myapp.domain.repository.implementation

import co.lateralview.myapp.domain.repository.interfaces.ParserManager
import com.google.gson.Gson
import java.lang.reflect.Type

class ParserManagerImpl(private val mGson: Gson) : ParserManager {

    override fun toJson(`object`: Any): String {
        return mGson.toJson(`object`)
    }

    override fun <T> fromJson(json: String, type: Class<T>): T {
        return mGson.fromJson(json, type)
    }

    override fun <T> fromJson(json: String, type: Type): T {
        return mGson.fromJson<T>(json, type)
    }
}