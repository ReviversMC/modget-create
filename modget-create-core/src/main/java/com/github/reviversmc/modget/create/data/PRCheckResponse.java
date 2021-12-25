package com.github.reviversmc.modget.create.data;

public class PRCheckResponse {
    //This is so ridiculous.

    private Data data;

    public PRCheckResponse() {

    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data.Repository.PullRequests.Nodes[] getPRNodes() { //Shortcut to get nodes.
        return data.repository.pullRequests.nodes;
    }

    private static class Data {

        private Repository repository;

        public Repository getRepository() {
            return repository;
        }

        public void setRepository(Repository repository) {
            this.repository = repository;
        }

        private static class Repository {

            private PullRequests pullRequests;

            public PullRequests getPullRequests() {
                return pullRequests;
            }

            public void setPullRequests(PullRequests pullRequests) {
                this.pullRequests = pullRequests;
            }

            private static class PullRequests {

                private Nodes[] nodes;

                public Nodes[] getNodes() {
                    return nodes;
                }

                public void setNodes(Nodes[] nodes) {
                    this.nodes = nodes;
                }

                private static class Nodes {

                    private String title;

                }
            }
        }
    }
}
