package co.lateralview.myapp.domain.repository.interfaces

import java.lang.reflect.Type

interface SharedPreferencesManager {

    fun saveBlocking(key: String, value: String): Boolean

    fun getString(key: String): String

    fun getString(key: String, defaultValue: String): String

    fun getInt(key: String, defaultValue: Int): Int

    operator fun <T> get(key: String, type: Class<T>): T?

    operator fun <T> get(key: String, type: Type): T?

    fun clear()
}