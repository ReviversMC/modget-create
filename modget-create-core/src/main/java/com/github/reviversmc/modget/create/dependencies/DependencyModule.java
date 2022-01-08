package com.github.reviversmc.modget.create.dependencies;

import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import org.kohsuke.github.connector.GitHubConnector;
import org.kohsuke.github.extras.okhttp3.OkHttpGitHubConnector;

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

    @Provides
    @Singleton
    static GitHubConnector okhttpGithubConnector(OkHttpClient okHttpClient) {
        return new OkHttpGitHubConnector(okHttpClient);
    }

    @Named("yaml")
    @Provides
    @Singleton
    static ObjectMapper yamlMapper() {
        return new ObjectMapper(new YAMLFactory()
                .disable(YAMLParser.Feature.EMPTY_STRING_AS_NULL)
                .disable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                .disable(YAMLGenerator.Feature.SPLIT_LINES)
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(YAMLGenerator.Feature.INDENT_ARRAYS)
                .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
        );
    }
}
