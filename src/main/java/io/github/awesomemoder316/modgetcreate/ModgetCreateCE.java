package io.github.awesomemoder316.modgetcreate;

import io.github.awesomemoder316.modgetcreate.commands.CommandManager;
import io.github.awesomemoder316.modgetcreate.commands.CommandManagerComponent;
import io.github.awesomemoder316.modgetcreate.commands.DaggerCommandManagerComponent;

import java.util.Scanner;

public class ModgetCreateCE {

    /*public static void main(String[] args) {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .cookieJar(new JavaNetCookieJar(cookieManager)).build();
        try {

            RequestBody body = new FormBody.Builder()
                    .add("client_id", " 0adbb600023664c217ab")
                            .add("scope", "public_repo")
                                    .build();

            Request req = new Request.Builder()
                    .url("https://github.com/login/oauth/authorize")
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Language", "en-US,en;q=0.8")
                    .addHeader("User-Agent", "Mozilla")
                    .post(body)
                    .build();

            Response response = okHttpClient.newCall(req).execute();

            //Desktop.getDesktop().browse(new URI(response.));


            System.out.println(response.body().string());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }*/

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CommandManagerComponent commandManagerComponent = DaggerCommandManagerComponent.create();
        CommandManager commandManager = commandManagerComponent.getCommandManager();

        //noinspection InfiniteLoopStatement, It is not an infinite loop as "Exit" command can be called.
        while (true) {
            System.out.print("Modget-Create-CE> ");
            commandManager.callCommand(scanner.nextLine());
        }
    }
}
