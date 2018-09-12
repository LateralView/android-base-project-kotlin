package co.lateralview.myapp.application

import android.app.Application
import co.lateralview.myapp.infrastructure.rest.NetModule
import co.lateralview.myapp.ui.activity.main.MainComponent
import co.lateralview.myapp.ui.activity.main.MainModule
import co.lateralview.myapp.ui.util.date.DateModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DateModule::class,
    NetModule::class])
interface AppComponent {
    fun inject(application: Application)

    fun plus(mainModule: MainModule): MainComponent
}