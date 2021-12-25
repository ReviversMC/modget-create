package com.github.reviversmc.modget.create.cli.commands;

import com.diogonunes.jcolor.Attribute;
import com.github.reviversmc.modget.create.github.TokenManager;
import com.github.reviversmc.modget.create.github.TokenOAuthGuider;
import com.github.reviversmc.modget.create.manifests.ManifestCreator;
import com.github.reviversmc.modget.create.manifests.ManifestCreatorFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
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

        //CF id, manifest creator, and MR id will definitely have a value, as they are mandatory values.
        Optional<String> optionalCurseforgeId = ArgObtainer.obtain(args, List.of("-cf", "--curseforge"));
        Optional<String> optionalModrinthId = ArgObtainer.obtain(args, List.of("-mr", "--modrinth"));
        Optional<String> optionalJarPath = ArgObtainer.obtain(args, List.of("-j", "--jar"));

        if (optionalCurseforgeId.isEmpty() ||
                optionalModrinthId.isEmpty() ||
                optionalJarPath.isEmpty()) { //Should never happen.

            System.out.println(
                    colorize(
                            "An unexpected error occurred!",
                            Attribute.RED_TEXT()
                    )
            );
            return;
        }

        ManifestCreator manifestCreator = manifestCreatorFactory.create(optionalJarPath.get());

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

        if (manifestCreator.isModPresent()) {
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

        //Start search for optional args
        Optional<String> optionalOutputFolder = ArgObtainer.obtain(args, List.of("-o", "--output"));
        File outputFolder;

        if (optionalOutputFolder.isPresent()) {
            outputFolder = new File(optionalOutputFolder.get());

            if (!outputFolder.exists() || !outputFolder.isDirectory()) {
                System.out.println(
                        colorize(
                                "The provided output directory is invalid!\n" +
                                        "If you meant to let modget-create do a PR for you, don't specify an output directory!",
                                Attribute.RED_TEXT()
                        )
                );
                return;
            }
        }

        Optional<String> optionalToken = ArgObtainer.obtain(args, List.of("-t", "--token"));
        String token;

        if (optionalToken.isPresent()) {
            token = optionalToken.get();
            if (!tokenManager.validateToken(token)) {
                System.out.println(
                        colorize(
                                "Your GitHub token is invalid! Please ensure that it has the " +
                                        "\"read:user\", and the \"public_repo\" scope.",
                                Attribute.RED_TEXT()
                        )
                );
            }
        } else {
            optionalToken = tokenManager.getToken();
            if (optionalToken.isEmpty()) tokenOAuthGuider.guide();
            if (optionalToken.isPresent()) token = optionalToken.get();
            else { //User rejected oAuth.
                //No token, which is required.
                System.out.println(
                        colorize(
                                "You have no output directory or valid GitHub token!" +
                                        "Please try again with a GitHub token, " +
                                        "or approve the authentication code when you are presented with it.",
                                Attribute.RED_TEXT()
                        )
                );
                return;
            }
        }

        Optional<InputStream> optionalManifestYamlInputStream = manifestCreator.createYaml(
                optionalModrinthId.get(), optionalCurseforgeId.get(), token
        );
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
        return List.of("-o", "--output", "-t", "--token");
    }

    @Override
    public List<List<String>> getRequiredParameters() {
        return List.of(
                List.of("-cf", "--curseforge"),
                List.of("-j", "--jar"),
                List.of("-mr", "--modrinth")
        );
    }
}
