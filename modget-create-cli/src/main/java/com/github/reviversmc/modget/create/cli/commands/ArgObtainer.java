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
    public static Optional<String> obtainFirst(Map<String, List<String>> args, List<String> toObtain) {
        for (Map.Entry<String, List<String>> attemptObtain : args.entrySet()) {
            if (toObtain.contains(attemptObtain.getKey()))
                return attemptObtain.getValue().isEmpty() ?
                        Optional.empty() : Optional.of(attemptObtain.getValue().get(0));
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
    public static List<String> obtainAll(Map<String, List<String>> args, List<String> toObtain) {
        List<String> values = new ArrayList<>();
        for (Map.Entry<String, List<String>> attemptObtain : args.entrySet()) {
            if (toObtain.contains(attemptObtain.getKey()))
                values.addAll(attemptObtain.getValue());
        }
        return values;
    }
}
