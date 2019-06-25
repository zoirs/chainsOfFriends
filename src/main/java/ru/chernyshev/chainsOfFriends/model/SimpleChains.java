package ru.chernyshev.chainsOfFriends.model;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

public class SimpleChains {

    private List<List<String>> chains = new ArrayList<>();

    private SimpleChains() {
    }

    public static class Builder {

        private final Integer firstUser;
        private final Integer lastUser;
        private List<Integer> chain;

        private SimpleChains result;
//        private static Builder builder;

        public Builder(Integer firstUser, Integer lastUser) {
            this.firstUser = firstUser;
            this.lastUser = lastUser;
            this.result = new SimpleChains();
        }

        public Builder startChain() {
            chain = new ArrayList<>();
            chain.add(firstUser);
            return this;
        }

        public Builder add(Integer link) {
//            if (chain.size() == 1 && chain.contains(link)) {
//                // из за нарушеной логики поиска
//                return this;
//            }
            chain.add(link);
            return this;
        }

        public Builder complete() {
            chain.add(lastUser);
            List<String> ids = chain.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            result.add(ids);
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

    public boolean hasChain(){
        return !isEmpty(getChains());
    }

}
