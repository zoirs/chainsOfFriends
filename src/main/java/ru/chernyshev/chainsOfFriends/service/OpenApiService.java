package ru.chernyshev.chainsOfFriends.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.chernyshev.chainsOfFriends.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenApiService {

    private static Logger logger = LoggerFactory.getLogger(OpenApiService.class);

    private final VkApiClient vk;
    private ServiceActor serviceActor;

    @Autowired
    public OpenApiService(VkApiClient vk,
                          @Value("${client.id}") int clientId,
                          @Value("${client.secret}") String clientSecret,
                          @Value("${app.secret}") String appSecret) {
        this.vk = vk;
        this.serviceActor = new ServiceActor(clientId, clientSecret, appSecret);
    }

    public List<User> get(String ...id) {
        List<UserXtrCounters> users;
        try {
            users = vk.users().get(serviceActor).userIds(id).fields(Fields.PHOTO_200_ORIG).execute();
        } catch (ApiException | ClientException e) {
            logger.error("Cant get user {}", e);
            return null;
        }
        if (CollectionUtils.isEmpty(users)) {
            logger.error("User not found {}", id);
            return null;
        }
        return users.stream().map(User::new).collect(Collectors.toList());
    }
}
