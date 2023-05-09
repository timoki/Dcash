package com.dmonster.dcash.di

import com.dmonster.dcash.BuildConfig
import com.dmonster.dcash.utils.AppInfo
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object ApiModule {
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
        logging: HttpLoggingInterceptor,
        deviceHeaderInfo: String,
        appInfo: AppInfo
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(logging)
        addInterceptor { chain ->
            val response = chain.request().newBuilder()
                .removeHeader("User-Agent")
                .addHeader("X-Device-Info", deviceHeaderInfo)
                .addHeader("User-Agent", appInfo.getUserAgent())
                .build()

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
}