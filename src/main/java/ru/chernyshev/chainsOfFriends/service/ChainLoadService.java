package ru.chernyshev.chainsOfFriends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.chernyshev.chainsOfFriends.model.FullChains;
import ru.chernyshev.chainsOfFriends.model.SimpleChains;
import ru.chernyshev.chainsOfFriends.model.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ChainLoadService {
    private final OpenApiService openApiService;

    @Autowired
    public ChainLoadService(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    public FullChains load(SimpleChains simpleChains) {
        if (!simpleChains.hasChain()) {
            return new FullChains(Collections.emptyList(), Collections.emptyList());
        }
        List<List<String>> chains = simpleChains.getChains().subList(0, Math.min(simpleChains.getChains().size(), 10));

        Set<String> allUserIds = new HashSet<>();
        for (List<String> chain : chains) {//todo to stream api?
            allUserIds.addAll(chain);
        }

        List<User> allUsers = openApiService.get(allUserIds.toArray(new String[0]));
        return new FullChains(chains, allUsers);
    }
}
