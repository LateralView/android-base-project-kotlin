package co.lateralview.myapp.application

import android.app.Application
import android.content.Context
import co.lateralview.myapp.ui.util.date.DateUtils
import co.lateralview.myapp.domain.repository.implementation.ParserManagerImpl
import co.lateralview.myapp.domain.repository.implementation.SharedPreferencesManagerImpl
import co.lateralview.myapp.domain.repository.interfaces.ParserManager
import co.lateralview.myapp.domain.repository.interfaces.SharedPreferencesManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesContext(): Context = application

    @Provides
    fun provideDateUtils(): DateUtils = DateUtils()

    @Provides
    @Singleton
    fun providesSharedPreferencesManager(application: Application, parserManager: ParserManager):
        SharedPreferencesManager = SharedPreferencesManagerImpl(application, parserManager)

    @Provides
    fun providesParserManager(gson: Gson): ParserManager {
        return ParserManagerImpl(gson)
    }
}
