package ru.chernyshev.chainsOfFriends;


import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.friends.responses.GetFieldsResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@RestController
public class HelloController {

    @Value("${client.id}")
    private int clientId;
    @Value("${server.host}")
    private String host;
    @Value("${client.secret}")
    private String clientSecret;
    @Autowired
    VkApiClient vk;

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, the time at the server is now " + new Date() + "\n";
    }

    @GetMapping("/api/login")
    public ModelAndView login() {
        return new ModelAndView("redirect:" + getOAuthUrl());
    }

    @GetMapping("/callback")
    public ModelAndView callback(@RequestParam String code) {
        try {
            UserAuthResponse authResponse = vk.oauth().userAuthorizationCodeFlow(clientId, clientSecret, getRedirectUri(), code).execute();
            return new ModelAndView("redirect:" + "/api/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/api/info")
    public ModelAndView info(@RequestParam String user, @RequestParam String token) {
        try {
            UserActor actor = new UserActor(Integer.parseInt(user), token);
            List<UserXtrCounters> getUsersResponse = vk.users().get(actor).userIds(user).execute();
            UserXtrCounters use1 = getUsersResponse.get(0);
            System.out.println(use1.getId() + " " + use1.getFirstName());

            GetFieldsResponse execute = vk.friends().get(actor, UserField.NICKNAME, UserField.CITY)
                    .userId(140891700).execute();

            for (UserXtrLists item : execute.getItems()) {
                System.out.println(item.getFirstName() + " " + item.getLastName() + " " + (item.getCity() != null ? item.getCity().getTitle() : " - "));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private String getOAuthUrl() {
        return "https://oauth.vk.com/authorize?client_id=" + clientId + "&display=page&redirect_uri=" + getRedirectUri() + "&scope=groups&response_type=code";
    }

    private String getRedirectUri() {
        return "http://" + host + "/callback";
    }
}
