package co.lateralview.myapp.ui.util.date

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DateModule {

    @Provides
    @Singleton
    fun provideDateUtils(): DateUtils = DateUtils()
}