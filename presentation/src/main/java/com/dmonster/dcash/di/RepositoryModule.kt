package com.dmonster.dcash.di

import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.repository.repositoryImpl.MemberRepositoryImpl
import com.dmonster.data.repository.repositoryImpl.TokenRepositoryImpl
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.domain.repository.MemberRepository
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
}