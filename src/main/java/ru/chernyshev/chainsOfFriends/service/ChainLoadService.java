package ru.chernyshev.chainsOfFriends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.chernyshev.chainsOfFriends.model.FullChains;
import ru.chernyshev.chainsOfFriends.model.SimpleChains;
import ru.chernyshev.chainsOfFriends.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChainLoadService {
    private final OpenApiService openApiService;

    @Autowired
    public ChainLoadService(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    public FullChains load(SimpleChains simpleChains) {
        List<List<String>> chains = simpleChains.getChains().subList(0, Math.min(simpleChains.getChains().size(), 10));

        List<String> s = new ArrayList<>();
        for (List<String> chain : chains) {//todo to stream api?
            s.addAll(chain);
        }

//        String ids = chains.stream().map(chain -> String.join(",", chain)).collect(Collectors.joining(","));
        List<User> users = openApiService.get(s.toArray(new String[s.size()]));
        return new FullChains(chains, users);
    }
}
