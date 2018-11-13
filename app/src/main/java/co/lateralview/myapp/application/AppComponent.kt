package co.lateralview.myapp.application

import co.lateralview.myapp.domain.repository.RepositoryModule
import co.lateralview.myapp.infrastructure.manager.di.ManagersModule
import co.lateralview.myapp.infrastructure.rest.NetModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Main component of the app, created and persisted in the Application class.
 *
 * Whenever a new module is created, it should be added to the list of modules.
 * [AndroidSupportInjectionModule] is the module from Dagger.Android that helps with the
 * generation and location of subcomponents.
 */
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBindingModule::class,
    NetModule::class,
    RepositoryModule::class,
    ManagersModule::class])
interface AppComponent : AndroidInjector<MyAppApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MyAppApplication>()
}
