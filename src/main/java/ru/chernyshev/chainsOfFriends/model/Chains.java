package ru.chernyshev.chainsOfFriends.model;

import java.util.ArrayList;
import java.util.List;

public class Chains {

    private List<List<String>> chains = new ArrayList();

    private Chains() {
    }

    public static class Builder {

        private final String u1;
        private final String u2;
        private List<String> chain;

        private Chains chains;
//        private static Builder builder;

        public Builder(String u1, String u2) {
            this.u1 = u1;
            this.u2 = u2;
            this.chains = new Chains();
        }

        public Builder startChain() {
            chain = new ArrayList<>();
            chain.add(u1);
            return this;
        }

        public Builder add(String id) {
            chain.add(id);
            return this;
        }

        public Builder complete() {
            chain.add(u2);
            chains.add(chain);
            return this;
        }

        public Chains build() {
            return chains;
        }
    }

    private void add(List<String> chain) {
        chains.add(chain);
    }

    public List<List<String>> getChains() {
        return chains;
    }
}
