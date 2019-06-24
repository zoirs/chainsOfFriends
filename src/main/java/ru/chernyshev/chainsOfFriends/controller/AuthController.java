package ru.chernyshev.chainsOfFriends.controller;


import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.chernyshev.chainsOfFriends.model.User;
import ru.chernyshev.chainsOfFriends.service.UserApiService;

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
        logger.info("login");
        return new ModelAndView("redirect:" + getOAuthUrl());
    }

    @GetMapping("/api/needAuth")
    public boolean needAuth() {
        logger.info("needAuth");
        return userApiService.getActor() == null;
    }

    @GetMapping("/api/getAuthUser")
    public User getAuthUser() {
        if (userApiService.getActor() == null) {
            logger.info("Not authorized");
            return null;
        }
        User authUser = userApiService.getUser();
        if (authUser == null) {
            logger.info("Can't get authorized user");
        }
        return authUser;
    }

    @GetMapping("/api/authurl")
    public String authurl() {
//        logger.info("authurl " + firstUser + " " + secondUser);
        return getOAuthUrl();
    }

    @GetMapping("/callback")
    public ModelAndView callback(@RequestParam String code) {
        logger.info("Callback. Get auth code {}", code);
        UserAuthResponse authResponse;
        try {
            authResponse = vk.oAuth().userAuthorizationCodeFlow(clientId, clientSecret, getRedirectUri(), code).execute();
        } catch (Exception e) {
            logger.error("Vk api error. Can't get user authorization code flow", e);
            return null;
        }
        logger.info("Callback. Get auth token {} for user {}", authResponse.getUserId(), authResponse.getAccessToken());
        userApiService.setActor(authResponse.getUserId(), authResponse.getAccessToken());

        return new ModelAndView("redirect:https://" + host + "/?authSuccess=true");

        //        return new ModelAndView("redirect:" + "http://localhost:3000/?authSuccess=true");
        //        return new ModelAndView("redirect:http://" + host + "/api/info?user=" + authResponse.getUserId());
    }

    private String getOAuthUrl() {
        return "https://oauth.vk.com/authorize?client_id=" + clientId + "&display=page&redirect_uri=" + getRedirectUri() + "&scope=friends&response_type=code";
    }

    private String getRedirectUri() {
        return "https://" + host + "/callback";
    }
}
