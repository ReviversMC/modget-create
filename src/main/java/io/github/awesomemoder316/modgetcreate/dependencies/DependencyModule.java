package io.github.awesomemoder316.modgetcreate.dependencies;

import com.squareup.moshi.Moshi;
import dagger.Binds;
import dagger.Module;
import okhttp3.OkHttpClient;

import javax.inject.Singleton;

@Module
public interface DependencyModule {
    @Binds
    @Singleton
    Moshi moshi(Moshi impl);

    @Binds
    @Singleton
    OkHttpClient okhttpClient(OkHttpClient impl);
}
