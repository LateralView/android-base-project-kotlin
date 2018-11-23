package co.lateralview.myapp.infrastructure.manager.implementation

import android.content.Context
import androidx.annotation.RawRes
import co.lateralview.myapp.infrastructure.manager.interfaces.FileManager

class FileManagerImpl(
    private val context: Context
) : FileManager {
    override fun getStringFromFile(@RawRes jsonResourceID: Int): String {
        var fileAsString = ""
        try {
            val inputStream = context.resources.openRawResource(jsonResourceID)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            fileAsString = String(buffer)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return fileAsString
    }
}