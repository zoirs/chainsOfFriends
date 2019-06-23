package ru.chernyshev.chainsOfFriends.model;

import java.util.ArrayList;
import java.util.List;

public class SimpleChains {

    private List<List<String>> chains = new ArrayList<>();

    private SimpleChains() {
    }

    public static class Builder {

        private final String firstUser;
        private final String lastUser;
        private List<String> chain;

        private SimpleChains result;
//        private static Builder builder;

        public Builder(String firstUser, String lastUser) {
            this.firstUser = firstUser;
            this.lastUser = lastUser;
            this.result = new SimpleChains();
        }

        public Builder startChain() {
            chain = new ArrayList<>();
            chain.add(firstUser);
            return this;
        }

        public Builder add(String link) {
            if (chain.size() == 1 && chain.contains(link)) {
                // из за нарушеной логики поиска
                return this;
            }
            chain.add(link);
            return this;
        }

        public Builder complete() {
            chain.add(lastUser);
            result.add(chain);
            return this;
        }

        public boolean hasChain(){
            return !result.getChains().isEmpty();
        }

        public SimpleChains build() {
            return result;
        }
    }

    private void add(List<String> chain) {
        chains.add(chain);
    }

    public List<List<String>> getChains() {
        return chains;
    }
}
