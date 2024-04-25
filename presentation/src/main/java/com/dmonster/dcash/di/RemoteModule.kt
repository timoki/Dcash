package com.dmonster.dcash.di

import android.content.Context
import com.dmonster.data.remote.api.HomeAPIService
import com.dmonster.data.remote.api.LoginAPIService
import com.dmonster.data.remote.api.MemberAPIService
import com.dmonster.data.remote.api.NewsAPIService
import com.dmonster.data.remote.api.PointAPIService
import com.dmonster.data.remote.api.TokenAPIService
import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.repository.datasource.impl.RemoteDataSourceImpl
import com.dmonster.dcash.BuildConfig
import com.dmonster.dcash.utils.AppInfo
import com.dmonster.dcash.utils.StaticData.tokenData
import com.dmonster.dcash.utils.TokenManager
import com.dmonster.dcash.utils.clearMalformedUrls
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun providePointAPIService(
        retrofit: Retrofit
    ): PointAPIService = retrofit.create(PointAPIService::class.java)

    @Singleton
    @Provides
    fun provideHomeAPIService(
        retrofit: Retrofit
    ): HomeAPIService = retrofit.create(HomeAPIService::class.java)

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
    fun provideNewsAPIService(
        retrofit: Retrofit
    ): NewsAPIService = retrofit.create(NewsAPIService::class.java)

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        loginAPIService: LoginAPIService,
        tokenAPIService: TokenAPIService,
        memberAPIService: MemberAPIService,
        newsAPIService: NewsAPIService,
        homeAPIService: HomeAPIService,
        pointAPIService: PointAPIService,
    ): RemoteDataSource = RemoteDataSourceImpl(
        loginAPIService,
        tokenAPIService,
        memberAPIService,
        newsAPIService,
        homeAPIService,
        pointAPIService,
    )

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        logging: HttpLoggingInterceptor,
        deviceHeaderInfo: String,
        appInfo: AppInfo,
        authenticator: Authenticator
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val response = chain.request().newBuilder().apply {
                val accessToken = tokenData.value.accessToken
                if (!accessToken.isNullOrEmpty()) {
                    addHeader("Authorization", "Bearer $accessToken")
                }
                removeHeader("User-Agent").addHeader("X-Device-Info", deviceHeaderInfo)
                addHeader("User-Agent", appInfo.getUserAgent())
            }.build()
            chain.proceed(response)
        }
        addInterceptor(logging)
        cache(
            Cache(
                directory = File(context.cacheDir, "http_cache"),
                maxSize = 50L * 1024L * 1024L
            ).clearMalformedUrls()
        )
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        //authenticator(authenticator)
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
    fun provideAuthenticator(
        tokenManager: TokenManager
    ): Authenticator = Authenticator { route, response ->
        if (tokenData.value.refreshToken != null) {
            tokenManager.getAccessToken()

            val newToken = tokenData.value.accessToken

            response.request.newBuilder()
                .header("Authorization", "$newToken")
                .build()
        }

        null
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