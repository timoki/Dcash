package com.dmonster.dcash.di

import com.dmonster.data.remote.api.LoginAPIService
import com.dmonster.data.remote.api.MemberAPIService
import com.dmonster.data.remote.api.TokenAPIService
import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.repository.datasource.impl.RemoteDataSourceImpl
import com.dmonster.dcash.BuildConfig
import com.dmonster.dcash.utils.AppInfo
import com.dmonster.dcash.view.main.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideLoginAPIService(
        retrofit: Retrofit
    ): LoginAPIService = retrofit.create(LoginAPIService::class.java)

    @Singleton
    @Provides
    fun provideTokenAPIService(
        retrofit: Retrofit
    ): TokenAPIService = retrofit.create(TokenAPIService::class.java)

    @Singleton
    @Provides
    fun provideMemberAPIService(
        retrofit: Retrofit
    ): MemberAPIService = retrofit.create(MemberAPIService::class.java)

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        loginAPIService: LoginAPIService,
        tokenAPIService: TokenAPIService,
        memberAPIService: MemberAPIService,
    ): RemoteDataSource = RemoteDataSourceImpl(
        loginAPIService,
        tokenAPIService,
        memberAPIService,
    )

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client)
            .addConverterFactory(gsonConverterFactory).build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        deviceHeaderInfo: String,
        appInfo: AppInfo,
        mainActivity: MainActivity
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(logging)
        addInterceptor { chain ->
            val response = chain.request().newBuilder()
                .addHeader("Authorization", mainActivity.getAccessToken())
                .removeHeader("User-Agent").addHeader("X-Device-Info", deviceHeaderInfo)
                .addHeader("User-Agent", appInfo.getUserAgent()).build()
            chain.proceed(response)
        }
    }.build()


    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideDeviceHeaderInfo(
        appInfo: AppInfo
    ): String =
        "{\"t\":\"${appInfo.OS}\"," + "\"v\":\"${appInfo.getVersionName()}\"," + "\"p\":\"${appInfo.getOriginApplicationId()}\"," + "\"i\":\"${appInfo.getDeviceId()}\"," + "\"s\":\"${BuildConfig.STORE_TYPE}\"}"
}