package ru.chernyshev.chainsOfFriends.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.chernyshev.chainsOfFriends.model.Chains;
import ru.chernyshev.chainsOfFriends.service.UserApiService;

@RestController
public class UserApiController {
    private static Logger logger = LoggerFactory.getLogger(UserApiService.class);

    private final UserApiService userApiService;

    @Autowired
    public UserApiController(UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    @GetMapping("/api/search")
    public ModelAndView search(@RequestParam String firstUser, @RequestParam String secondUser) throws ClientException, ApiException {

        int u1 = Integer.parseInt(firstUser);
        int u2 = Integer.parseInt(secondUser);


        Chains chains = userApiService.search(u1, u2, 0);
        logger.info(chains.getChains().toString());


        return null;
    }



}
