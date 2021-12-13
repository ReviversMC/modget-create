package io.github.awesomemoder316.modgetcreate.dependencies;

import dagger.Binds;
import dagger.Module;
import okhttp3.OkHttpClient;

@Module
public interface DependencyModule {
    @Binds
    OkHttpClient getOkhttpClient(OkHttpClient impl);
}
