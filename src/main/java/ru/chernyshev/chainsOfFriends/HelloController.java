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


}
