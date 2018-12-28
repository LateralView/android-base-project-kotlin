package co.lateralview.myapp.infrastructure.manager.interfaces

import androidx.annotation.RawRes

interface FileManager {
    fun getStringFromFile(@RawRes jsonResourceID: Int): String
}