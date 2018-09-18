package co.lateralview.myapp.domain.repository.interfaces

import java.lang.reflect.Type

interface ParserManager {
    fun toJson(`object`: Any): String

    fun <T> fromJson(json: String, type: Class<T>): T

    fun <T> fromJson(json: String, type: Type): T
}
