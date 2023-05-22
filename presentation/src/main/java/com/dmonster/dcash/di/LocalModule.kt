package com.dmonster.dcash.di

import android.content.Context
import androidx.room.Room
import com.dmonster.data.local.dao.ConfigDao
import com.dmonster.data.local.dao.paging.NewsDao
import com.dmonster.data.local.dao.paging.RemoteKeysDao
import com.dmonster.data.local.database.MyDatabase
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.data.local.entity.paging.RemoteKeys
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
    fun provideLocalDataSource(
        configDao: ConfigDao,
        newsDao: NewsDao,
        remoteKeys: RemoteKeysDao,
    ): LocalDataSource = LocalDataSourceImpl(
        configDao,
        newsDao,
        remoteKeys,
    )

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MyDatabase = Room.databaseBuilder(
        context,
        MyDatabase::class.java,
        "dcash.db"
    ).fallbackToDestructiveMigration()
        .enableMultiInstanceInvalidation()
        .build()

    @Singleton
    @Provides
    fun provideConfigDao(
        database: MyDatabase
    ): ConfigDao = database.configDao()

    @Singleton
    @Provides
    fun provideNewsDao(
        database: MyDatabase
    ): NewsDao = database.newsDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(
        database: MyDatabase
    ): RemoteKeysDao = database.remoteKeys()
}