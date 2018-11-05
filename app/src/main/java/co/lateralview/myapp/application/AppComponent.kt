package co.lateralview.myapp.application

import android.app.Application
import co.lateralview.myapp.domain.repository.RepositoryModule
import co.lateralview.myapp.infrastructure.manager.di.ManagersModule
import co.lateralview.myapp.infrastructure.rest.NetModule
import co.lateralview.myapp.ui.activity.main.MainComponent
import co.lateralview.myapp.ui.activity.main.MainModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetModule::class,
    RepositoryModule::class,
    ManagersModule::class])
interface AppComponent {
    fun inject(application: Application)

    fun plus(mainModule: MainModule): MainComponent
}
