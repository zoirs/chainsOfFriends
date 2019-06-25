package ru.chernyshev.chainsOfFriends.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class FullChains {

    private List<List<User>> chains = new ArrayList<>();

    //todo посути это просто конвертер, нет необходимости в экземпляре объекта
    public FullChains(List<List<String>> chainsIds, List<User> users) {
        for (List<String> chainIds : chainsIds) {
            List<User> collect = chainIds.stream()
                    .map(id -> users.stream().filter(u -> u.getId().toString().equals(id)).findFirst().get())
                    .collect(toList());
            chains.add(collect);
        }
    }

    public List<List<User>> getChains() {
        return chains;
    }

}
