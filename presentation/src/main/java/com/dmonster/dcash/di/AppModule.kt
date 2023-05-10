package com.dmonster.dcash.di

import android.content.Context
import com.dmonster.dcash.utils.AppInfo
import com.dmonster.dcash.view.main.MainActivity
import com.dmonster.data.utils.ErrorCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideMainActivity(): MainActivity {
        return MainActivity()
    }

    @Singleton
    @Provides
    fun provideAppInfo(@ApplicationContext context: Context): AppInfo = AppInfo(context)

    @Singleton
    @Provides
    fun provideErrorCallback(): ErrorCallback {
        return ErrorCallback()
    }
}