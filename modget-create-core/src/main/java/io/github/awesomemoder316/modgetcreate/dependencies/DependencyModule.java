package io.github.awesomemoder316.modgetcreate.dependencies;

import com.squareup.moshi.Moshi;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import javax.inject.Singleton;

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
