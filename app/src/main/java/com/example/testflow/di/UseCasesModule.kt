package com.example.testflow.di

import com.example.testflow.domain.usecases.GetExampleItemsUseCase
import com.example.testflow.domain.usecases.GetExampleItemsUseCaseImpl
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
    abstract fun bindGetExampleItemsUseCase(impl: GetExampleItemsUseCaseImpl): GetExampleItemsUseCase
}