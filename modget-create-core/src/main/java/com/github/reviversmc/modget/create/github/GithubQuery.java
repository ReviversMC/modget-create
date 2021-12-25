package com.github.reviversmc.modget.create.github;

public enum GithubQuery {

    CONFIRM_LOGIN(
            "" + //Exists solely for readability.
                    "query {" +
                    "  viewer {" +
                    "   login" +
                    "  }" +
                    "}"
    ),
    GET_OPEN_PRS(
            "" + //Exists solely for readability.
                    "query {" +
                    "  repository(owner: \"reviversmc\", name: \"modget-manifests\") {" +
                    "    url" +
                    //Previous PR probably out-of-date and needs a bump if past 200.
                    "    pullRequests(first: 200, states: OPEN) {" +
                    "      nodes {" +
                    "        title" +
                    "      }" +
                    "    }" +
                    "  }"
    );

    private final String query;

    GithubQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
