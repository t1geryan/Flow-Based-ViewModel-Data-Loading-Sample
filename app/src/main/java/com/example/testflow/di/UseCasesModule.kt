package com.example.testflow.di

import com.example.testflow.domain.usecases.GetDevicesUseCase
import com.example.testflow.domain.usecases.GetDevicesUseCaseImpl
import com.example.testflow.domain.usecases.GetRandomNumberUseCase
import com.example.testflow.domain.usecases.GetRandomNumberUseCaseImpl
import com.example.testflow.domain.usecases.GetUsersUseCase
import com.example.testflow.domain.usecases.GetUsersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModule {
    @Binds
    @Singleton
    abstract fun bindGetExampleItemsUseCase(impl: GetDevicesUseCaseImpl): GetDevicesUseCase

    @Binds
    @Singleton
    abstract fun bindGetRandomNumberUseCase(impl: GetRandomNumberUseCaseImpl): GetRandomNumberUseCase

    @Binds
    @Singleton
    abstract fun bindGetUsersUseCase(impl: GetUsersUseCaseImpl): GetUsersUseCase
}