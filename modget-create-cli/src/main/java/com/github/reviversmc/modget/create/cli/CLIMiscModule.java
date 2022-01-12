package com.github.reviversmc.modget.create.cli;

import com.github.reviversmc.modget.create.github.TokenOAuthGuider;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import javax.inject.Singleton;
import java.io.IOException;

@Module
public interface CLIMiscModule {
    @Binds
    TokenOAuthGuider cLIGithubOAuthGuider(CLIGithubOAuthGuider cliGithubOAuthGuider);

    @Provides
    @Singleton
    static Terminal terminal() {
        try {
            return TerminalBuilder.terminal();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Provides
    static LineReader lineReader(Terminal terminal) {
        return LineReaderBuilder.builder()
                .appName("Modget Create CLI")
                .terminal(terminal)
                .build();
    }
}
