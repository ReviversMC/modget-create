package com.github.reviversmc.modget.create.dependencies;

import javax.inject.Singleton;

import com.squareup.moshi.Moshi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public interface DependencyModule {
    @Provides
    @Singleton
    static Moshi moshi() {
        return new Moshi.Builder().build();
    }

    @Provides
    @Singleton
    static OkHttpClient okhttpClient() {
        return new OkHttpClient.Builder().build();
    }
}
