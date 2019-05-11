package ru.chernyshev.chainsOfFriends;


import com.google.gson.JsonElement;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.ServiceClientCredentialsFlowResponse;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.friends.responses.GetFieldsResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.friends.FriendsGetQueryWithFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.chernyshev.chainsOfFriends.model.User;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HelloController {

    @Value("${client.id}")
    private int clientId;
    @Value("${server.host}")
    private String host;
    @Value("${client.secret}")
    private String clientSecret;
    @Value("${app.secret}")
    private String appSecret;
    @Autowired
    VkApiClient vk;

    private UserActor actor;

//    @GetMapping("/api/hello")
//    public String hello() {
//        System.out.println("======== hello =======");
//        return "Hello, the time at the server is now " + new Date() + "\n";
//    }


//    @GetMapping("/api/login")
//    public ModelAndView login() {
//        System.out.println("======== login =======");
//        return new ModelAndView("redirect:" + getOAuthUrl());
//    }

//    @GetMapping("/api/authurl")
//    public String authurl() {
//        System.out.println("======== login =======");
//        return getOAuthUrl();
//    }

//    @GetMapping("/callback")
//    public ModelAndView callback(@RequestParam String code) {
//        System.out.println("======== callback =======");
//        try {
//            UserAuthResponse authResponse = vk.oAuth().userAuthorizationCodeFlow(clientId, clientSecret, getRedirectUri(), code).execute();
//            return new ModelAndView("redirect:" + "/api/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }

//    @GetMapping("/api/info")
//    public ModelAndView info(@RequestParam String user, @RequestParam String token) {
//        System.out.println("======== infon =======");
//        try {
//            actor = new UserActor(Integer.parseInt(user), token);
//            List<UserXtrCounters> getUsersResponse = vk.users().get(actor).userIds(user).execute();
//            UserXtrCounters use1 = getUsersResponse.get(0);
//            System.out.println(use1.getId() + " " + use1.getFirstName());
//
////            GetFieldsResponseOverride execute = vk.friends().get(actor, UserField.NICKNAME, UserField.CITY)
////                    .userId(140891700).execute();
////
////            for (UserXtrLists item : execute.getItems()) {
////                System.out.println(item.getFirstName() + " " + item.getLastName() + " " + (item.getCity() != null ? item.getCity().getTitle() : " - "));
////            }
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return new ModelAndView("redirect:" + "/");
//    }




//    private String getOAuthUrl() {
//        return "https://oauth.vk.com/authorize?client_id=" + clientId + "&display=page&redirect_uri=" + getRedirectUri() + "&scope=friends&response_type=code";
//    }
//
//    private String getRedirectUri() {
//        return "http://" + host + "/callback";
//    }
}
