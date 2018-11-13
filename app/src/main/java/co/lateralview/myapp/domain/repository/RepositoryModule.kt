package co.lateralview.myapp.domain.repository

import co.lateralview.myapp.domain.repository.implementation.FirebaseSessionRepository
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSessionRepository(): SessionRepository {
        return FirebaseSessionRepository(FirebaseAuth.getInstance())
    }
}