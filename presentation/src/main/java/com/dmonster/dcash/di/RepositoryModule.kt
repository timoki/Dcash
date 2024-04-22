package com.dmonster.dcash.di

import com.dmonster.data.local.database.MyDatabase
import com.dmonster.data.repository.datasource.LocalDataSource
import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.repository.repositoryImpl.HomeRepositoryImpl
import com.dmonster.data.repository.repositoryImpl.MemberRepositoryImpl
import com.dmonster.data.repository.repositoryImpl.NewsRepositoryImpl
import com.dmonster.data.repository.repositoryImpl.TokenRepositoryImpl
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.domain.repository.HomeRepository
import com.dmonster.domain.repository.MemberRepository
import com.dmonster.domain.repository.NewsRepository
import com.dmonster.domain.repository.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideTokenRepository(
        remoteDataSource: RemoteDataSource,
        errorCallback: ErrorCallback
    ): TokenRepository = TokenRepositoryImpl(
        remoteDataSource,
        errorCallback
    )

    @Singleton
    @Provides
    fun provideMemberRepository(
        remoteDataSource: RemoteDataSource,
        errorCallback: ErrorCallback
    ): MemberRepository = MemberRepositoryImpl(
        remoteDataSource,
        errorCallback
    )

    @Singleton
    @Provides
    fun provideNewsRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        database: MyDatabase,
        errorCallback: ErrorCallback,
    ): NewsRepository = NewsRepositoryImpl(
        remoteDataSource,
        localDataSource,
        database,
        errorCallback,
    )

    @Singleton
    @Provides
    fun provideHomeRepository(
        remoteDataSource: RemoteDataSource,
        errorCallback: ErrorCallback
    ): HomeRepository = HomeRepositoryImpl(
        remoteDataSource,
        errorCallback
    )
}