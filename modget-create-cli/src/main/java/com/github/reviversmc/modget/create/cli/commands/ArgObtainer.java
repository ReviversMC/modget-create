package com.github.reviversmc.modget.create.cli.commands;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArgObtainer {

    private ArgObtainer() {

    }

    public static Optional<String> obtain(Map<String, Optional<String>> args, List<String> toObtain) {
        for (String attemptObtain : toObtain) {
            Optional<String> value = args.getOrDefault(attemptObtain, Optional.empty());
            if (value.isPresent()) return value;
        }
        return Optional.empty();
    }
}
