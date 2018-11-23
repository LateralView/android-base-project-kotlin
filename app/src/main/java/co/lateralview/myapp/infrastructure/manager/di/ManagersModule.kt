package co.lateralview.myapp.infrastructure.manager.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import co.lateralview.myapp.infrastructure.manager.implementation.FileManagerImpl
import co.lateralview.myapp.infrastructure.manager.implementation.FirebaseAuthenticationManager
import co.lateralview.myapp.infrastructure.manager.implementation.InternetManager
import co.lateralview.myapp.infrastructure.manager.interfaces.AuthenticationManager
import co.lateralview.myapp.infrastructure.manager.interfaces.FileManager
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ManagersModule {

    @Provides
    fun providesInternetManager(application: Application): InternetManager {
        return InternetManager(application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }

    @Provides
    @Singleton
    fun provideAuthenticationManager(sessionRepository: SessionRepository): AuthenticationManager =
        FirebaseAuthenticationManager(FirebaseAuth.getInstance(), sessionRepository)

    @Provides
    fun providesFileManager(context: Context): FileManager = FileManagerImpl(context)
}