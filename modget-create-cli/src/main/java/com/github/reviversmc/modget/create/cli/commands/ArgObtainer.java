package com.github.reviversmc.modget.create.cli.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArgObtainer {

    private ArgObtainer() {

    }

    /**
     * Obtain the first occurrence of the argument found.
     *
     * @param args     The list of arguments to check.
     * @param toObtain The arguments to find.
     * @return An {@link Optional} with a value if found, or an empty {@link Optional} otherwise.
     */
    public static Optional<String> obtainFirst(Map<String, Optional<String>> args, List<String> toObtain) {
        for (String attemptObtain : toObtain) {
            Optional<String> value = args.getOrDefault(attemptObtain, Optional.empty());
            if (value.isPresent()) return value;
        }
        return Optional.empty();
    }

    /**
     * Obtain the first occurrence of the argument found.
     *
     * @param args     The list of arguments to check.
     * @param toObtain The arguments to find.
     * @return A list of all found values.
     */
    public static List<String> obtainAll(Map<String, Optional<String>> args, List<String> toObtain) {
        List<String> values = new ArrayList<>();
        for (String attemptObtain : toObtain) {
            Optional<String> value = args.getOrDefault(attemptObtain, Optional.empty());
            value.ifPresent(values::add);
        }
        return values;
    }
}
