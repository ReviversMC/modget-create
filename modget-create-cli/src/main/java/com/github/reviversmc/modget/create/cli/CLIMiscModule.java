package com.github.reviversmc.modget.create.cli;

import com.github.reviversmc.modget.create.github.TokenOAuthGuider;
import dagger.Binds;
import dagger.Module;

@Module
public interface CLIMiscModule {
    @Binds
    TokenOAuthGuider cLIGithubOAuthGuider(CLIGithubOAuthGuider cliGithubOAuthGuider);
}
