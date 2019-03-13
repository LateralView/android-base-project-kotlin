package ${packageName}

import android.app.Activity
import ${applicationPackage}.ui.util.di.ActivityScoped
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ${className}Module {

    @Binds
    @ActivityScoped
    abstract fun provideView(activity: ${className}Activity): ${className}Contract.View

    @Binds
    @ActivityScoped
    abstract fun provideActivity(activity: ${className}Activity): Activity

    @Module
    companion object {
        @JvmStatic
        @Provides
        @ActivityScoped
        fun providePresenter(view: ${className}Contract.View): ${className}Contract.Presenter {
            return ${className}Presenter(view)
        }
    }
}
