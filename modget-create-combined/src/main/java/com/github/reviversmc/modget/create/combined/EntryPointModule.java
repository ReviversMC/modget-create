package com.github.reviversmc.modget.create.combined;

import com.github.reviversmc.modget.create.cli.ModgetCreateCli;
import com.github.reviversmc.modget.create.gui.ModgetCreateGui;
import com.github.reviversmc.modget.create.main.EntryPoint;
import dagger.Binds;
import dagger.Module;

import javax.inject.Named;

@Module
public interface EntryPointModule {

    @Binds
    @Named("cli")
    EntryPoint modgetCreateCli(ModgetCreateCli modgetCreateCli);

    @Binds
    @Named("gui")
    EntryPoint modgetCreateGui(ModgetCreateGui modgetCreateGui);

}
