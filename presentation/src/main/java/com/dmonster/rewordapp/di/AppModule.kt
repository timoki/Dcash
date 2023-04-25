package com.dmonster.rewordapp.di

import android.content.Context
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.rewordapp.view.main.MainActivity
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
    fun provideDataStoreModule(
        @ApplicationContext context: Context
    ): DataStoreModule {
        return DataStoreModule(context)
    }

    @Singleton
    @Provides
    fun provideMainActivity(): MainActivity {
        return MainActivity()
    }
}