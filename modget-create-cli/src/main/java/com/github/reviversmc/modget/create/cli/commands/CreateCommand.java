package com.github.reviversmc.modget.create.cli.commands;

import com.diogonunes.jcolor.Attribute;
import com.github.reviversmc.modget.create.data.ModStatus;
import com.github.reviversmc.modget.create.github.TokenManager;
import com.github.reviversmc.modget.create.github.TokenOAuthGuider;
import com.github.reviversmc.modget.create.manifests.ManifestCreator;
import com.github.reviversmc.modget.create.manifests.ManifestCreatorFactory;

import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.diogonunes.jcolor.Ansi.colorize;

public class CreateCommand implements Command {

    private final ManifestCreatorFactory manifestCreatorFactory;
    private final TokenManager tokenManager;
    private final TokenOAuthGuider tokenOAuthGuider;

    @Inject
    public CreateCommand(
            ManifestCreatorFactory manifestCreatorFactory,
            TokenManager tokenManager,
            TokenOAuthGuider tokenOAuthGuider
    ) {
        this.manifestCreatorFactory = manifestCreatorFactory;
        this.tokenManager = tokenManager;
        this.tokenOAuthGuider = tokenOAuthGuider;
    }

    @Override
    public void onCommand(Map<String, Optional<String>> args) {

        //All of these will definitely have a value, as they are mandatory values.
        Optional<String> optionalCurseforgeId = ArgObtainer.obtainFirst(args, List.of("-cf", "--curseforge"));
        Optional<String> optionalModrinthId = ArgObtainer.obtainFirst(args, List.of("-mr", "--modrinth"));
        Optional<String> optionalJarPath = ArgObtainer.obtainFirst(args, List.of("-j", "-jar", "--jar"));
        Optional<String> optionalStatus = ArgObtainer.obtainFirst(args, List.of("-s", "--status"));
        List<String> modVersions = ArgObtainer.obtainAll(args, List.of("-v", "-ver", "--version"));
        Optional<String> optionalWiki = ArgObtainer.obtainFirst(args, List.of("-w", "--wiki"));

        if (optionalCurseforgeId.isEmpty() ||
                optionalModrinthId.isEmpty() ||
                optionalJarPath.isEmpty() ||
                optionalStatus.isEmpty() ||
                modVersions.isEmpty() ||
                optionalWiki.isEmpty()
        ) { //Should never happen.

            System.out.println(
                    colorize(
                            "An unexpected error occurred!",
                            Attribute.RED_TEXT()
                    )
            );
            return;
        }

        ModStatus modStatus;

        try {
            modStatus = ModStatus.valueOf(optionalStatus.get().toUpperCase());

        } catch (IllegalArgumentException ex) {
            System.out.println(
                    colorize(
                            "You provided an invalid mod status! (argument -s/--status) " +
                                    "Please specify \"ABANDONED\", \"ACTIVE\", \"EOL\", or \"UNKNOWN\".",
                            Attribute.RED_TEXT()
                    )
            );
            return;
        }


        //Start search for optional args
        Optional<String> optionalOutputFolder = ArgObtainer.obtainFirst(args, List.of("-o", "--output"));
        File outputFolder = null;

        if (optionalOutputFolder.isPresent()) {
            outputFolder = new File(optionalOutputFolder.get());

            if (!outputFolder.exists() || !outputFolder.isDirectory()) {
                System.out.println(
                        colorize(
                                "The provided output directory is invalid!\n" +
                                        "If you meant to let modget-create do a PR for you, don't specify an output directory! (-o/--output)",
                                Attribute.RED_TEXT()
                        )
                );
                return;
            }
        }

        Optional<String> optionalToken = ArgObtainer.obtainFirst(args, List.of("-t", "--token"));
        String token;

        if (optionalToken.isPresent()) {
            token = optionalToken.get();
            if (!tokenManager.validateToken(token)) {
                System.out.println(
                        colorize(
                                "Your GitHub token is invalid! Please ensure that it has the " +
                                        "\"public_repo\" scope.",
                                Attribute.RED_TEXT()
                        )
                );
                return;
            }
        } else {
            optionalToken = tokenManager.getToken();
            if (optionalToken.isEmpty()) {
                tokenOAuthGuider.guide();
                optionalToken = tokenManager.getToken();
            }

            if (optionalToken.isPresent()) token = optionalToken.get();
            else { //User rejected oAuth.
                //No token, which is required.
                System.out.println(
                        colorize(
                                "You have no valid GitHub token!" +
                                        "Please try again with a GitHub token, " +
                                        "or approve the authentication code when you are presented with it.",
                                Attribute.RED_TEXT()
                        )
                );
                return;
            }
        }

        List<String> updateAlternatives = ArgObtainer.obtainAll(
                args, List.of("-ua", "--update-alternatives")
        );

        ManifestCreator manifestCreator = manifestCreatorFactory.create(
                modVersions,
                updateAlternatives,
                modStatus,
                token,
                optionalCurseforgeId.get(),
                optionalJarPath.get(),
                optionalModrinthId.get(),
                optionalWiki.get()
        );

        if (!manifestCreator.isUsable()) {
            System.out.println(
                    colorize(
                            "You did not specify a valid Fabric mod using \"-j <path-to-mod>\" " +
                                    "or \"--jar <path-to-mod>\"!",
                            Attribute.RED_TEXT()
                    )
            );
            return;
        }

        boolean forceRecreate = args.containsKey("-force") || args.containsKey("--force-recreate");

        if (forceRecreate) {
            System.out.println(
                    colorize(
                            "Warning: You are creating a manifest for mod that already has a manifest!",
                            Attribute.YELLOW_TEXT()
                    )
            );
        }

        if (manifestCreator.isModPresent() && !forceRecreate) {
            System.out.println(
                    colorize(
                            "Thanks for wanting to contribute, but the manifest for this mod already exists," +
                                    " or is currently being PR-ed by someone else!\n" +
                                    "Cancelling operation...",
                            Attribute.YELLOW_TEXT()
                    )
            );
            return;
        }

        Optional<String> optionalMainManifest = manifestCreator.createMainYaml(forceRecreate);
        Optional<String> optionalLookupTable = manifestCreator.createLookupTable(forceRecreate);

        if (optionalMainManifest.isEmpty() || optionalLookupTable.isEmpty()) {
            System.out.println(
                    colorize(
                            "Something went wrong! Please try again.",
                            Attribute.RED_TEXT()
                    )
            );
            return;
        }

        try {
            if (optionalOutputFolder.isPresent()) {
                FileOutputStream mainYmlStream = new FileOutputStream(
                        outputFolder.getAbsolutePath() + File.separator + "main.yml"
                );
                mainYmlStream.write(optionalMainManifest.get().getBytes(StandardCharsets.UTF_8));
                mainYmlStream.close();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<String> getCommandNames() {
        return List.of("create", "manifest", "publish", "upload");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getOptionalParameters() {
        return List.of("-force",
                "--force-recreate",
                "-o",
                "--output",
                "-t",
                "--token",
                "-ua",
                "--update-alternatives"
        );
    }

    @Override
    public List<List<String>> getRequiredParameters() {
        return List.of(
                List.of("-cf", "--curseforge"),
                List.of("-j", "-jar", "--jar"),
                List.of("-mr", "--modrinth"),
                List.of("-s", "--status"),
                List.of("-v", "-ver", "--version"),
                List.of("-w", "--wiki")
        );
    }
}
