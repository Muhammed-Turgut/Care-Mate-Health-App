package com.muhammedturgut.caremate.data.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiClient {

    // Hugging Face Spaces API URL
    private const val BASE_URL = "https://harezmii-caremate-health-api.hf.space/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        // Logging interceptor - sadece debug modda aktif et
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val originalRequest = chain.request()

                // Request headers ekle
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json, text/event-stream")
                    .addHeader("User-Agent", "CareMate-Android-App/1.0")
                    .build()

                val response = chain.proceed(newRequest)

                // Response'u logla
                android.util.Log.d("ApiClient", "Request: ${originalRequest.method} ${originalRequest.url}")
                android.util.Log.d("ApiClient", "Response: ${response.code} ${response.message}")

                response
            }
            // Timeout ayarlarını artır - Hugging Face yavaş olabilir
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            // Retry ayarları
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideHFService(retrofit: Retrofit): HFService {
        return retrofit.create(HFService::class.java)
    }
}