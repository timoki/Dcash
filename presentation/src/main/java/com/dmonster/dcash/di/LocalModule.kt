package com.dmonster.dcash.di

import android.content.Context
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.data.repository.datasource.LocalDataSource
import com.dmonster.data.repository.datasource.impl.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Singleton
    @Provides
    fun provideDataStoreModule(
        @ApplicationContext context: Context
    ): DataStoreModule = DataStoreModule(context)

    @Singleton
    @Provides
    fun provideLocalDataSource(): LocalDataSource = LocalDataSourceImpl()
}