package com.github.reviversmc.modget.create.combined;

import com.github.reviversmc.modget.create.main.EntryPoint;

public class ModgetCreateCombined {

    public static void main(String[] args) {

        EntryPointComponent daggerEntryPointComponent = DaggerEntryPointComponent.create();
        EntryPoint entryPoint;

        if (args.length == 0 || !args[0].equalsIgnoreCase("cli"))
            entryPoint = daggerEntryPointComponent.getGuiEntryPoint();
        else
            entryPoint = daggerEntryPointComponent.getCliEntryPoint();

        entryPoint.start();
    }

}
