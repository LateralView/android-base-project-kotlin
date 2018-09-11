package co.lateralview.myapp.ui.activity.main

import co.lateralview.myapp.ui.util.di.ActivityScoped
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = [MainModule::class])
interface MainComponent {
    fun inject(activity: MainActivity)
}