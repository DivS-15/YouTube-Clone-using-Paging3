package com.divyansh.clone.youtube.injection

import com.divyansh.clone.youtube.data.remote.api.VideosRemoteInterface
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit =
        Retrofit.Builder().baseUrl(VideosRemoteInterface.BASE_URL).addConverterFactory(
            MoshiConverterFactory.create(
                moshi
            )
        ).build()

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): VideosRemoteInterface =
        retrofit.create(VideosRemoteInterface::class.java)

    @Singleton
    @Provides
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

}
