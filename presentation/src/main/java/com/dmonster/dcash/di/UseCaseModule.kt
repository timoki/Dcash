package com.dmonster.dcash.di

import com.dmonster.domain.repository.HomeRepository
import com.dmonster.domain.repository.MemberRepository
import com.dmonster.domain.repository.NewsRepository
import com.dmonster.domain.repository.TokenRepository
import com.dmonster.domain.usecase.ChangeRefreshTokenUseCase
import com.dmonster.domain.usecase.GetAccessTokenUseCase
import com.dmonster.domain.usecase.GetHomeDataUseCase
import com.dmonster.domain.usecase.GetMemberInfoUseCase
import com.dmonster.domain.usecase.GetNewsListUseCase
import com.dmonster.domain.usecase.RequestLoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideRequestLoginUseCase(
        memberRepository: MemberRepository
    ): RequestLoginUseCase = RequestLoginUseCase(memberRepository)

    @Provides
    @ViewModelScoped
    fun provideGetAccessTokenUseCase(
        tokenRepository: TokenRepository
    ): GetAccessTokenUseCase = GetAccessTokenUseCase(tokenRepository)

    @Provides
    @ViewModelScoped
    fun provideChangeRefreshTokenUseCase(
        tokenRepository: TokenRepository
    ): ChangeRefreshTokenUseCase = ChangeRefreshTokenUseCase(tokenRepository)

    @Provides
    @ViewModelScoped
    fun provideGetMemberInfoUseCase(
        memberRepository: MemberRepository
    ): GetMemberInfoUseCase = GetMemberInfoUseCase(memberRepository)

    @Provides
    @ViewModelScoped
    fun provideGetNewsListUseCase(
        newsRepository: NewsRepository
    ): GetNewsListUseCase = GetNewsListUseCase(newsRepository)

    @Provides
    @ViewModelScoped
    fun provideGetHomeDataUseCase(
        homeRepository: HomeRepository
    ): GetHomeDataUseCase = GetHomeDataUseCase(homeRepository)
}