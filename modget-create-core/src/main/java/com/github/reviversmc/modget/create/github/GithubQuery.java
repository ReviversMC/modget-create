package com.github.reviversmc.modget.create.github;

public enum GithubQuery {

    CONFIRM_LOGIN(
            "" + //Exists solely for readability.
                    "query {" +
                    "  viewer {" +
                    "   login" +
                    "  }" +
                    "}"
    );

    private String query;

    GithubQuery(String query) {

    }

    public String getQuery() {
        return query;
    }
}
