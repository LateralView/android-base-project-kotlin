package co.lateralview.myapp.infrastructure.manager.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import co.lateralview.myapp.application.MyAppApplication
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import co.lateralview.myapp.infrastructure.manager.implementation.CalendarManagerImpl
import co.lateralview.myapp.infrastructure.manager.implementation.FileManagerImpl
import co.lateralview.myapp.infrastructure.manager.implementation.FirebaseAuthenticationManager
import co.lateralview.myapp.infrastructure.manager.implementation.InternetManager
import co.lateralview.myapp.infrastructure.manager.implementation.LocationManagerImpl
import co.lateralview.myapp.infrastructure.manager.implementation.PermissionsManagerImpl
import co.lateralview.myapp.infrastructure.manager.implementation.SettingsManagerImpl
import co.lateralview.myapp.infrastructure.manager.interfaces.AuthenticationManager
import co.lateralview.myapp.infrastructure.manager.interfaces.CalendarManager
import co.lateralview.myapp.infrastructure.manager.interfaces.FileManager
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
    fun providesPermissionsManager(activity: Activity): PermissionsManager = PermissionsManagerImpl(activity)

    @Provides
    fun providesSettingsManager(application: MyAppApplication): SettingsManager =
        SettingsManagerImpl(LocationServices.getSettingsClient(application))

    @Provides
    fun providesLocationManager(
        application: MyAppApplication,
        activity: Activity,
        permissionsManager: PermissionsManager,
        settingsManager: SettingsManager
    ): LocationManager =
        LocationManagerImpl(LocationServices.getFusedLocationProviderClient(application),
            settingsManager,
            permissionsManager,
            activity)

    @Provides
    fun providesFileManager(context: Context): FileManager = FileManagerImpl(context)

    @Provides
    fun providesCalendarManager(): CalendarManager = CalendarManagerImpl()
}