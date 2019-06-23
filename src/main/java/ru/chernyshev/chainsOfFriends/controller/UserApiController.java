package ru.chernyshev.chainsOfFriends.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.chernyshev.chainsOfFriends.model.FullChains;
import ru.chernyshev.chainsOfFriends.model.SimpleChains;
import ru.chernyshev.chainsOfFriends.model.User;
import ru.chernyshev.chainsOfFriends.service.ChainLoadService;
import ru.chernyshev.chainsOfFriends.service.UserApiService;

import java.util.List;

@RestController
public class UserApiController {
    private static Logger logger = LoggerFactory.getLogger(UserApiService.class);

    private final UserApiService userApiService;
    private final ChainLoadService chainLoadService;

    @Autowired
    public UserApiController(UserApiService userApiService, ChainLoadService chainLoadService) {
        this.userApiService = userApiService;
        this.chainLoadService = chainLoadService;
    }

    @GetMapping("/api/search")
    public ResponseEntity<List<List<User>>> search(@RequestParam String firstUser, @RequestParam String secondUser) throws ClientException, ApiException {

        if (StringUtils.isEmpty(firstUser) || StringUtils.isEmpty(secondUser)) {
            logger.error("Is empty {}, {}", firstUser, secondUser);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        int u1;
        int u2;

        try {
            u1 = Integer.parseInt(firstUser);
            u2 = Integer.parseInt(secondUser);
        } catch (NumberFormatException e) {
            logger.error("Is not number {}, {}", firstUser, secondUser);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        SimpleChains chains = userApiService.search(u1, u2);
        logger.info(chains.getChains().toString());
        FullChains load = chainLoadService.load(chains);
        return ResponseEntity.ok(load.getChains());
    }


}
