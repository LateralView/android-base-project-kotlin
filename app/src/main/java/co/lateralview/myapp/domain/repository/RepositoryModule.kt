package co.lateralview.myapp.domain.repository

import co.lateralview.myapp.application.AppModule
import co.lateralview.myapp.domain.repository.implementation.FirebaseSessionRepository
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import co.lateralview.myapp.domain.repository.interfaces.SharedPreferencesManager
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSessionRepository(sharedPreferencesManager: SharedPreferencesManager): SessionRepository {
        return FirebaseSessionRepository(FirebaseAuth.getInstance())
    }
}