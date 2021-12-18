package com.github.reviversmc.modget.create.dependencies;

import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public interface DependencyModule {

    @Named("json")
    @Provides
    @Singleton
    static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    static OkHttpClient okhttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Named("yaml")
    @Provides
    @Singleton
    static ObjectMapper yamlMapper() {
        return new ObjectMapper(new YAMLFactory());
    }
}
