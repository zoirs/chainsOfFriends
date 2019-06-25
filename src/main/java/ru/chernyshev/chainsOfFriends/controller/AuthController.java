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
    private final String protocol;

    private final VkApiClient vk;
    private final UserApiService userApiService;

    @Autowired
    public AuthController(VkApiClient vk,
                          UserApiService userApiService,
                          @Value("${client.id}") int clientId,
                          @Value("${server.host}") String host,
                          @Value("${client.secret}") String clientSecret,
                          @Value("${protocol:https}") String protocol) {
        this.vk = vk;
        this.userApiService = userApiService;
        this.clientId = clientId;
        this.host = host;
        this.clientSecret = clientSecret;
        this.protocol = protocol;
    }

    @GetMapping("/api/login")
    public ModelAndView login() {
        logger.info("login");
        return new ModelAndView("redirect:" + getOAuthUrl());
    }

    @GetMapping("/api/needAuth")
    public boolean needAuth() {
        logger.info("needAuth");
        return userApiService.needAuth();
    }

    @GetMapping("/api/getAuthUser")
    public User getAuthUser() {
        if (userApiService.needAuth()) {
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

        return new ModelAndView("redirect:" + protocol + "://" + host + "/?authSuccess=true");
    }

    private String getOAuthUrl() {
        return "https://oauth.vk.com/authorize?client_id=" + clientId + "&display=page&redirect_uri=" + getRedirectUri() + "&scope=friends&response_type=code";
    }

    private String getRedirectUri() {
        return protocol + "://" + host + "/callback";
    }
}
