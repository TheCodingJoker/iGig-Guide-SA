package com.wecodesa.igigguidesa.explore.di;

import com.wecodesa.igigguidesa.explore.data.repository.EventRepositoryImpl;
import com.wecodesa.igigguidesa.explore.domain.repository.EventRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract EventRepository bindEventRepository(EventRepositoryImpl impl);
}