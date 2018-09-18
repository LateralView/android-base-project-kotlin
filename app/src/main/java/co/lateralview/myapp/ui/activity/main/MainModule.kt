package co.lateralview.myapp.ui.activity.main

import co.lateralview.myapp.ui.util.di.ActivityScoped
import dagger.Module
import dagger.Provides

@Module
class MainModule(private val activity: MainActivity) {

    @Provides
    @ActivityScoped
    fun provideContext() = activity

    @Provides
    @ActivityScoped
    fun provideView(): MainContract.View = activity
}