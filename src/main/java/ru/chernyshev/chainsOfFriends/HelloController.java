package ru.chernyshev.chainsOfFriends;


import com.google.gson.JsonElement;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
    @Autowired
    VkApiClient vk;

    private UserActor actor;

    @GetMapping("/api/hello")
    public String hello() {
        System.out.println("======== hello =======");
        return "Hello, the time at the server is now " + new Date() + "\n";
    }

    @GetMapping("/api/login")
    public ModelAndView login() {
        System.out.println("======== login =======");
        return new ModelAndView("redirect:" + getOAuthUrl());
    }

    @GetMapping("/api/authurl")
    public String authurl() {
        System.out.println("======== login =======");
        return getOAuthUrl();
    }

    @GetMapping("/callback")
    public ModelAndView callback(@RequestParam String code) {
        System.out.println("======== callback =======");
        try {
            UserAuthResponse authResponse = vk.oAuth().userAuthorizationCodeFlow(clientId, clientSecret, getRedirectUri(), code).execute();
            return new ModelAndView("redirect:" + "/api/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/api/info")
    public ModelAndView info(@RequestParam String user, @RequestParam String token) {
        System.out.println("======== infon =======");
        try {
            actor = new UserActor(Integer.parseInt(user), token);
            List<UserXtrCounters> getUsersResponse = vk.users().get(actor).userIds(user).execute();
            UserXtrCounters use1 = getUsersResponse.get(0);
            System.out.println(use1.getId() + " " + use1.getFirstName());

//            GetFieldsResponseOverride execute = vk.friends().get(actor, UserField.NICKNAME, UserField.CITY)
//                    .userId(140891700).execute();
//
//            for (UserXtrLists item : execute.getItems()) {
//                System.out.println(item.getFirstName() + " " + item.getLastName() + " " + (item.getCity() != null ? item.getCity().getTitle() : " - "));
//            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return new ModelAndView("redirect:" + "/");
    }


    @GetMapping("/api/search")
    public ModelAndView search(@RequestParam String firstUser, @RequestParam String secondUser) throws ClientException, ApiException {

        int u1 = Integer.parseInt(firstUser);
        int u2 = Integer.parseInt(secondUser);


        String s = s(u1, u2, 0);
        System.out.println(s);


        return null;
    }

    private String s(int u1, int u2, int m) throws ApiException, ClientException {

        sleep();

        GetFieldsResponseOverride execute1 = new FriendsGetQueryWithFieldsOverride(vk, actor, Fields.CITY, Fields.MAIDEN_NAME).userId(u1).execute();
        Integer count1 = execute1.getCount();
        System.out.println("У пользователя " + u1 + " " + count1 + " друзей");

        GetFieldsResponseOverride execute2 = new FriendsGetQueryWithFieldsOverride(vk, actor, Fields.CITY, Fields.MAIDEN_NAME).userId(u2).execute();
        Integer count2 = execute2.getCount();
        System.out.println("У пользователя " + u1 + " " + count2 + " друзей");

        if (execute2.getItems().stream().anyMatch(userXtrLists -> userXtrLists.getId().equals(u1)) ||
                execute1.getItems().stream().anyMatch(userXtrLists -> userXtrLists.getId().equals(u2))) {
            System.out.println("Они друзья");
            return null;
        }

        boolean firstEger = count1 > count2;

        String e2 = execute2.getItems().stream()
                .filter(userXtrLists -> StringUtils.isEmpty(userXtrLists.getDeactivated())&& (!userXtrLists.getClosed()))
                .limit(firstEger ? Math.min(count2, 20) : Math.min(100, count2))
                .map(userXtrLists -> String.valueOf(userXtrLists.getId()))
                .collect(Collectors.joining(","));

        String e1 = execute1.getItems().stream()
                .filter(userXtrLists -> StringUtils.isEmpty(userXtrLists.getDeactivated()) && (!userXtrLists.getClosed())) //отфильтровать по доступности?
                .limit(firstEger ? Math.min(100, count1) : Math.min(count1, 20) )
                .map(userXtrLists -> String.valueOf(userXtrLists.getId()))
                .collect(Collectors.joining(","));

        System.out.println("У активные друзья" + u1 + " " + e1);
        System.out.println("У активные друзья" + u2 + " " + e2);

        sleep();

        String procedure = "" +
                "var omk=[:e1],\n" +
                "i=[:e2],\n" +
                "jqw=omk.length,\n" +
                "puf,\n" +
                "tm=0,\n" +
                "shx=[];\n" +
                "while(tm<jqw) {\n" +
                "    puf=API.friends.getMutual({\"source_uid\":omk[tm],\"target_uids\":i});\n" +
                "    shx.push(puf);\n" +
                "    tm=tm+1;\n" +
                "}\n" +
                "return shx;";

        if (e2.length() < e1.length()) {
            procedure = procedure.replace(":e1", e2);
            procedure = procedure.replace(":e2", e1);
        } else {
            procedure = procedure.replace(":e1", e1);
            procedure = procedure.replace(":e2", e2);
        }

        System.out.println("вызываем процедуру " + procedure);

        JsonElement response = vk.execute().code(actor, procedure)
                .execute();
        System.out.println("Результат: " + response.toString());

        return null;
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getOAuthUrl() {
        return "https://oauth.vk.com/authorize?client_id=" + clientId + "&display=page&redirect_uri=" + getRedirectUri() + "&scope=friends&response_type=code";
    }

    private String getRedirectUri() {
        return "http://" + host + "/callback";
    }
}
