package com.github.reviversmc.modget.create.cli.commands;

import com.diogonunes.jcolor.Attribute;
import com.github.reviversmc.modget.create.apicalls.ModrinthQuery;
import com.github.reviversmc.modget.create.apicalls.ModrinthQueryFactory;
import com.github.reviversmc.modget.create.data.ModStatus;
import com.github.reviversmc.modget.create.data.ModrinthV1ModPojo;
import com.github.reviversmc.modget.create.github.TokenManager;
import com.github.reviversmc.modget.create.github.TokenOAuthGuider;
import com.github.reviversmc.modget.create.manifests.ManifestCreator;
import com.github.reviversmc.modget.create.manifests.ManifestCreatorFactory;
import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.project.CurseProject;

import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.diogonunes.jcolor.Ansi.colorize;

public class CreateCommand implements Command {

    private final ManifestCreatorFactory manifestCreatorFactory;
    private final ModrinthQueryFactory modrinthQueryFactory;
    private final TokenManager tokenManager;
    private final TokenOAuthGuider tokenOAuthGuider;

    @Inject
    public CreateCommand(
            ManifestCreatorFactory manifestCreatorFactory,
            ModrinthQueryFactory modrinthQueryFactory,
            TokenManager tokenManager,
            TokenOAuthGuider tokenOAuthGuider
    ) {
        this.manifestCreatorFactory = manifestCreatorFactory;
        this.modrinthQueryFactory = modrinthQueryFactory;
        this.tokenManager = tokenManager;
        this.tokenOAuthGuider = tokenOAuthGuider;
    }

    @Override
    public void onCommand(Map<String, List<String>> args) {

        //All of these will definitely have a value, as they are mandatory values.
        Optional<String> optionalJarPath = ArgObtainer.obtainFirst(args, List.of("-j", "-jar", "--jar"));
        Optional<String> optionalStatus = ArgObtainer.obtainFirst(args, List.of("-s", "--status"));
        List<String> modVersions = ArgObtainer.obtainAll(args, List.of("-v", "-ver", "--version"));
        Optional<String> optionalWiki = ArgObtainer.obtainFirst(args, List.of("-w", "--wiki"));

        if (optionalJarPath.isEmpty()) {
            System.out.println(
                    colorize(
                            "You need to provide the path to the mod! (argument -j/-jar/--jar)",
                            Attribute.RED_TEXT()
                    )
            );
            return;
        }
        if (optionalStatus.isEmpty()) {
            System.out.println(
                    colorize(
                            "You need to provide the mod status! (argument -s/--status) " +
                                    "Please specify \"ABANDONED\", \"ACTIVE\", \"EOL\", or \"UNKNOWN\".",
                            Attribute.RED_TEXT()
                    )
            );
            return;
        }
        if (modVersions.isEmpty()) {
            System.out.println(
                    colorize(
                            "You need to provide all versions of the mod! (argument -v/-ver/--version)",
                            Attribute.RED_TEXT()
                    )
            );
            return;
        }
        if (optionalWiki.isEmpty()) {
            System.out.println(
                    colorize(
                            "You need to provide the wiki page for this mod! (argument -w/--wiki)",
                            Attribute.RED_TEXT()
                    )
            );
            return;
        }

        Optional<String> optionalCurseforgeId = ArgObtainer.obtainFirst(args, List.of("-cf", "--curseforge"));
        Optional<String> optionalModrinthId = ArgObtainer.obtainFirst(args, List.of("-mr", "--modrinth"));

        if (optionalCurseforgeId.isEmpty()) {
            System.out.println(
                    colorize("WARNING: You did not specify a Curseforge id for this mod!" +
                                    "If this mod does not have a Curseforge page, " +
                                    "this warning can be safely ignored.",
                            Attribute.YELLOW_TEXT()
                    )
            );
        } else {
            try {
                int curseId = Integer.parseInt(optionalCurseforgeId.get());
                Optional<CurseProject> optionalCurseProject = CurseAPI.project(curseId);

                if (optionalCurseProject.isEmpty()) {
                    System.out.println(
                            colorize(
                                    "The Curseforge id provided does not link to " +
                                            "any Curseforge page!",
                                    Attribute.RED_TEXT()
                            )
                    );
                    return;
                }

                System.out.println(
                        colorize(
                                "Mod identified on Curseforge as " + optionalCurseProject.get().name(),
                                Attribute.GREEN_TEXT()
                        )
                );

            } catch (CurseException ex) {
                ex.printStackTrace();
                System.out.println(
                        colorize(
                                "An error occurred " +
                                        "when trying to obtain this project's Curseforge site!",
                                Attribute.RED_TEXT()
                        )
                );
                return;
            } catch (NumberFormatException ex) {
                System.out.println(
                        colorize(
                                "The Curseforge id should only consist of numbers!",
                                Attribute.RED_TEXT()
                        )
                );
                return;
            }
        }

        if (optionalModrinthId.isEmpty()) {
            System.out.println(
                    colorize("WARNING: You did not specify a Modrinth id for this mod!" +
                                    "If this mod does not have a Modrinth page, " +
                                    "this warning can be safely ignored.",
                            Attribute.YELLOW_TEXT()
                    )
            );
        } else {
            ModrinthQuery modrinthQuery = modrinthQueryFactory.create(optionalModrinthId.get());

            try {
                Optional<ModrinthV1ModPojo> optionalModrinthModPojo = modrinthQuery.getMod();

                if (optionalModrinthModPojo.isEmpty()) {
                    System.out.println(
                            colorize(
                                    "The provided modrinth id does not link to any Modrinth site!",
                                    Attribute.RED_TEXT()
                            )
                    );
                    return;
                }

                System.out.println(
                        colorize(
                                "Mod identified on Modrinth as " +
                                        optionalModrinthModPojo.get().getTitle(),
                                Attribute.GREEN_TEXT()
                        )
                );

            } catch (IOException ex) {
                System.out.println(
                        colorize(
                                "An error occurred when attempting to communicate with Modrinth!",
                                Attribute.RED_TEXT()
                        )
                );
                return;
            }
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
        Optional<String> optionalUserSpecifiedOutputFolder = ArgObtainer.obtainFirst(args, List.of("-o", "--output"));
        File userSpecifiedOutputFolder = null;

        if (optionalUserSpecifiedOutputFolder.isPresent()) {
            userSpecifiedOutputFolder = new File(optionalUserSpecifiedOutputFolder.get());

            if (!userSpecifiedOutputFolder.exists() || !userSpecifiedOutputFolder.isDirectory()) {
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
            try {
                if (!tokenManager.validateToken(token)) {
                    System.out.println(
                            colorize(
                                    "Your GitHub token is invalid! Please ensure that it has the " +
                                            "\"public_repo\" scope as well.",
                                    Attribute.RED_TEXT()
                            )
                    );
                    return;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        } else {
            if (optionalUserSpecifiedOutputFolder.isEmpty()) {
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
            } else {
                token = null;
                System.out.println(
                        colorize(
                                "Warning: You are running modget-create unauthenticated! " +
                                        "This means that you will not be able to use automatic PRs, " +
                                        "and that you may be rate limited.\n" +
                                        "If you are experiencing repeated failures with MGC, " +
                                        "you may be rate limited.",
                                Attribute.YELLOW_TEXT()
                        )
                );
            }
        }

        List<String> updateAlternatives = ArgObtainer.obtainAll(
                args, List.of("-ua", "--update-alternatives")
        );

        ManifestCreator manifestCreator = manifestCreatorFactory.create(
                modVersions,
                updateAlternatives,
                modStatus,
                token, //Null value is fine.
                optionalCurseforgeId.orElse(""),
                optionalJarPath.get(),
                optionalModrinthId.orElse(""),
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

        try {
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
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        Optional<String> optionalMainManifest;
        Optional<String> optionalLookupTable;

        try {
            optionalMainManifest = manifestCreator.createMainYaml(forceRecreate);
            optionalLookupTable = manifestCreator.createLookupTable(forceRecreate);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

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
            if (optionalUserSpecifiedOutputFolder.isPresent()) {
                String modId = manifestCreator.getModId().orElseThrow();
                File modOutputFolder = new File(userSpecifiedOutputFolder + File.separator + modId);
                FileOutputStream mainYmlStream = new FileOutputStream(
                        modOutputFolder.getAbsolutePath() + File.separator + "main.yml"
                );
                mainYmlStream.write(optionalMainManifest.get().getBytes(StandardCharsets.UTF_8));
                mainYmlStream.close();

                FileOutputStream lookupTableStream = new FileOutputStream(
                        modOutputFolder.getAbsolutePath() + File.separator + "lookup-table.yaml"
                );
                lookupTableStream.write(optionalLookupTable.get().getBytes(StandardCharsets.UTF_8));
                lookupTableStream.close();
            } else {
                //TODO(The automatic PR function is not yet complete! Please specify an output folder for the time being.)
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
        return "This command creates all the files necessary for a new manifest.\n" +
                "Parameter definitions:\n" +
                "-cf, --curseforge <id>:        The Curseforge id of the mod your wish to create a manifest for. " +
                "Pass without value if Not Applicable.\n" +
                "-force, --force-recreate:      Whether to force the recreation of manifests " +
                "for mods that already have submitted manifests. No values accepted.\n" +
                "-j, -jar, --jar <path/to/mod>: The path to the mod you wish" +
                "to create a manifest for. You can drag and drop the mod onto your terminal.\n" +
                "-mr, --modrinth <id/slug>:     The Modrinth id/slug of the mod " +
                "you wish to create a manifest for. Pass without value if Not Applicable.\n" +
                "-o, --output <path/to/folder>: The output folder where this command should create the " +
                "appropriate files. A subfolder will be created for the mod. " +
                "Do NOT use if you want automatic PRs\n" +
                "-s, --status (abandoned/active/eol/unknown): The status of the mod, " +
                "as specified by the 4 options.\n" +
                "-t, --token <token>:           Use a GitHub Personal Access Token instead of " +
                "the token cached by modget-create\n" +
                "-ua, --update-alternatives <package>: The package of an update alternative to this mod, " +
                "if it is discontinued. You can use this parameter multiple times.\n" +
                "-v, -ver, --version <mod version>: A version of this mod that exists. " +
                "You can use this parameter multiple times. " +
                "Please use this parameter to specify all versions of this mod.\n" +
                "-w, --wiki <wiki site>:        The wiki site for the mod.";
    }

    @Override
    public List<String> getOptionalParameters() {
        return List.of(
                "-force",
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
