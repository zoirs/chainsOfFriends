package ru.chernyshev.chainsOfFriends.controller;


import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.chernyshev.chainsOfFriends.service.UserApiService;

import java.util.List;

@RestController
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final int clientId;
    private final String host;
    private final String clientSecret;

    private final VkApiClient vk;
    private final UserApiService userApiService;

    @Autowired
    public AuthController(VkApiClient vk,
                          UserApiService userApiService,
                          @Value("${client.id}") int clientId,
                          @Value("${server.host}") String host,
                          @Value("${client.secret}") String clientSecret) {
        this.vk = vk;
        this.userApiService = userApiService;
        this.clientId = clientId;
        this.host = host;
        this.clientSecret = clientSecret;
    }

    @GetMapping("/api/login")
    public ModelAndView login() {
        logger.trace("login");
        return new ModelAndView("redirect:" + getOAuthUrl());
    }

    @GetMapping("/api/needAuth")
    public boolean needAuth() {
        logger.trace("needAuth");
        return userApiService.getActor() == null;
    }

    @GetMapping("/api/authurl")
    public String authurl(@RequestParam String firstUser, @RequestParam String secondUser) {
        logger.info("authurl " + firstUser + " " + secondUser);
        return getOAuthUrl();
    }

    @GetMapping("/callback")
    public ModelAndView callback(@RequestParam String code) {
        logger.trace("callback");
        UserAuthResponse authResponse = null;
        try {
            authResponse = vk.oAuth().userAuthorizationCodeFlow(clientId, clientSecret, getRedirectUri(), code).execute();
        } catch (Exception e) {
            logger.error("error get user authorization code flow", e);
            return null;
        }
        userApiService.setActor(authResponse.getUserId(), authResponse.getAccessToken());
        return new ModelAndView("redirect:" + "/api/info?user=" + authResponse.getUserId());
    }

    @GetMapping("/api/info")
    public ModelAndView info(@RequestParam String user) {
        logger.trace("info");
        UserActor actor = userApiService.getActor();

        if (actor == null) {
            logger.warn("User actor is null");
            return null;
        }
        List<UserXtrCounters> getUsersResponse;
        try {
            getUsersResponse = vk.users().get(actor).userIds(user).execute();
        } catch (Exception e) {
            logger.error("Cant get user", e);
            return null;
        }
        UserXtrCounters use1 = getUsersResponse.get(0);
        logger.info(use1.getId() + " " + use1.getFirstName());
        return new ModelAndView("redirect:" + "/?authSuccess=true");
    }

    private String getOAuthUrl() {
        return "https://oauth.vk.com/authorize?client_id=" + clientId + "&display=page&redirect_uri=" + getRedirectUri() + "&scope=friends&response_type=code";
    }

    private String getRedirectUri() {
        return "http://" + host + "/callback";
    }
}
