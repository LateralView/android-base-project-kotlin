package co.lateralview.myapp.ui.activity.main

import android.app.Activity
import co.lateralview.myapp.infrastructure.manager.di.ManagersModule
import co.lateralview.myapp.infrastructure.manager.interfaces.LocationManager
import co.lateralview.myapp.ui.util.di.ActivityScoped
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [ManagersModule::class])
abstract class MainModule {

    @Binds
    @ActivityScoped
    abstract fun provideView(mainActivity: MainActivity): MainContract.View

    @Binds
    @ActivityScoped
    abstract fun provideActivity(activity: MainActivity): Activity

    /**
     * The method annotated with `@Provides` needs an instance, so we are making it static
     *
     * Reference: https://google.github.io/dagger/faq.html#why-cant-binds-and-instance-provides-methods-go-in-the-same-module
     */
    @Module
    companion object {
        @JvmStatic
        @Provides
        @ActivityScoped
        fun providePresenter(
            mainView: MainContract.View,
            locationManager: LocationManager
        ): MainContract.Presenter {
            return MainPresenter(mainView, locationManager)
        }
    }
}