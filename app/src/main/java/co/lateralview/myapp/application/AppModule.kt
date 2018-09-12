package co.lateralview.myapp.application

import android.app.Application
import android.content.Context
import co.lateralview.myapp.ui.util.date.DateUtils
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
}