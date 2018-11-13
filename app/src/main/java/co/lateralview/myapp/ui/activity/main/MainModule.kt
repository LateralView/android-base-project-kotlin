package co.lateralview.myapp.ui.activity.main

import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class MainModule {

    @Binds
    abstract fun provideMainView(mainActivity: MainActivity): MainContract.View

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideMainPresenter(mainView: MainContract.View): MainContract.Presenter {
            return MainPresenter(mainView)
        }
    }
}