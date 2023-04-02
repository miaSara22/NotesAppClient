package com.miaekebom.mynotesapp

import android.app.Application
import com.miaekebom.mynotesapp.model.IRepository
import com.miaekebom.mynotesapp.model.IServerManager
import com.miaekebom.mynotesapp.model.MyServer
import com.miaekebom.mynotesapp.model.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Container {

    @Provides
    @Singleton
    fun provideRepository(context: Application, @Named("myCustomServer") serverManager: IServerManager): IRepository {
        return Repository(context, serverManager)
    }

    @Provides
    @Singleton
    @Named("myCustomServer")
    fun provideMyCustomServer(context: Application): IServerManager {
        return MyServer(context)
    }
}