package co.lateralview.myapp.domain.repository

import co.lateralview.myapp.domain.repository.implementation.SessionRepositoryImpl
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSessionRepository(sessionRepository: SessionRepositoryImpl): SessionRepository {
        return sessionRepository
    }
}