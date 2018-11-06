package co.lateralview.myapp.infrastructure.manager.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import co.lateralview.myapp.infrastructure.manager.implementation.FirebaseAuthenticationManager
import co.lateralview.myapp.infrastructure.manager.implementation.InternetManager
import co.lateralview.myapp.infrastructure.manager.implementation.LocationManagerImpl
import co.lateralview.myapp.infrastructure.manager.implementation.PermissionsManagerImpl
import co.lateralview.myapp.infrastructure.manager.implementation.SettingsManagerImpl
import co.lateralview.myapp.infrastructure.manager.interfaces.AuthenticationManager
import co.lateralview.myapp.infrastructure.manager.interfaces.LocationManager
import co.lateralview.myapp.infrastructure.manager.interfaces.PermissionsManager
import co.lateralview.myapp.infrastructure.manager.interfaces.SettingsManager
import com.google.android.gms.location.LocationServices
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
    @Singleton
    fun providesPermissionsManager(): PermissionsManager = PermissionsManagerImpl()

    @Provides
    @Singleton
    fun providesSettingsManager(application: Application): SettingsManager =
        SettingsManagerImpl(LocationServices.getSettingsClient(application))

    @Provides
    @Singleton
    fun providesLocationManager(
        application: Application,
        permissionsManager: PermissionsManager,
        settingsManager: SettingsManager
    ): LocationManager =
        LocationManagerImpl(LocationServices.getFusedLocationProviderClient(application),
            settingsManager,
            permissionsManager,
            application)
}