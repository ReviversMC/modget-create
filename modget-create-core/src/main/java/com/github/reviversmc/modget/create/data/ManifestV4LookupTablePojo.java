package com.github.reviversmc.modget.create.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class ManifestV4LookupTablePojo {

    private Entry[] entries;

    public Entry[] getEntries() {
        return entries;
    }

    public void setEntries(Entry[] entries) {
        this.entries = entries;
    }

    public static class Entry {
        private String id;
        private String[] alternativeNames;
        private String[] tags;
        private Package[] packages;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String[] getAlternativeNames() {
            return alternativeNames;
        }

        public void setAlternativeNames(String[] alternativeNames) {
            this.alternativeNames = alternativeNames;
        }

        public String[] getTags() {
            return tags;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }

        public Package[] getPackages() {
            return packages;
        }

        public void setPackages(Package[] packages) {
            this.packages = packages;
        }

        public static class Package {
            private String packageId;
            private String[] loaders;

            public String getPackageId() {
                return packageId;
            }

            public void setPackageId(String packageId) {
                this.packageId = packageId;
            }

            public String[] getLoaders() {
                return loaders;
            }

            public void setLoaders(String[] loaders) {
                this.loaders = loaders;
            }
        }
    }
}
