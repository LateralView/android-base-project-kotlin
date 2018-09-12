package co.lateralview.myapp.infrastructure.rest

import android.app.Application
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import co.lateralview.myapp.infrastructure.manager.InternetManager
import co.lateralview.myapp.infrastructure.networking.RetrofitManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    fun provideRetrofitManager(
        application: Application,
        gson: Gson,
        sessionRepository: SessionRepository,
        internetManager: InternetManager
    ): RetrofitManager = RetrofitManager(application, gson, sessionRepository, internetManager)
}
