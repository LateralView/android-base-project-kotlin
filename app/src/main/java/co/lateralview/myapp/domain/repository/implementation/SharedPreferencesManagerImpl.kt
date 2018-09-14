package co.lateralview.myapp.domain.repository.implementation

import android.content.Context
import android.content.SharedPreferences
import co.lateralview.myapp.domain.repository.interfaces.ParserManager
import co.lateralview.myapp.domain.repository.interfaces.SharedPreferencesManager
import java.lang.reflect.Type

class SharedPreferencesManagerImpl(
    context: Context,
    private val mParserManager: ParserManager,
    fileName: String = DEFAULT_FILE_NAME
) : SharedPreferencesManager {
    private val mSharedPreferences: SharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun save(key: String, value: Boolean) {
        mSharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun save(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun save(key: String, value: Int) {
        mSharedPreferences.edit().putInt(key, value).apply()
    }

    override fun saveBlocking(key: String, value: String): Boolean {
        return mSharedPreferences.edit().putString(key, value).commit()
    }

    fun getBoolean(key: String): Boolean {
        return mSharedPreferences.getBoolean(key, false)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    override fun getString(key: String): String {
        return getString(key, "")
    }

    override fun getString(key: String, defaultValue: String): String {
        return mSharedPreferences.getString(key, defaultValue) ?: ""
    }

    fun getInt(key: String): Int {
        return getInt(key, -1)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    fun <T : Any> save(key: String, model: T) {
        mSharedPreferences.edit().putString(key, mParserManager.toJson(model)).apply()
    }

    override fun <T> get(key: String, type: Class<T>): T? {
        val json = getString(key)
        return if (json !== "") mParserManager.fromJson(json, type) else null
    }

    override fun <T> get(key: String, type: Type): T? {
        val json = getString(key)
        return if (json !== "") mParserManager.fromJson<Any>(json, type) as T else null
    }

    override fun clear() {
        mSharedPreferences.edit().clear().apply()
    }

    companion object {
        const val DEFAULT_FILE_NAME = "co.lateralview.myapp.sharedPreferences"
    }
}