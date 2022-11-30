package com.tawajood.vet.di
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.tawajood.vet.api.RetrofitApi
import com.tawajood.vet.repository.Repository


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): RetrofitApi {
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(
            LoggingInterceptor.Builder().setLevel(Level.BASIC)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
        )
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(RetrofitApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RetrofitApi::class.java)
    }




    @Provides
    @Singleton
    fun provideRepository(retrofitApi: RetrofitApi): Repository {
        return Repository(retrofitApi)
    }


}