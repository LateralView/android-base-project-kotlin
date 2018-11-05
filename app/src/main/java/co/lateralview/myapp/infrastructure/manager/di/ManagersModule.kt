package co.lateralview.myapp.infrastructure.manager.di

import co.lateralview.myapp.domain.repository.RepositoryModule
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import co.lateralview.myapp.infrastructure.manager.implementation.FirebaseAuthenticationManager
import co.lateralview.myapp.infrastructure.manager.interfaces.AuthenticationManager
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
class ManagersModule {

    @Provides
    @Singleton
    fun provideAuthenticationManager(sessionRepository: SessionRepository): AuthenticationManager =
        FirebaseAuthenticationManager(FirebaseAuth.getInstance(), sessionRepository)
}