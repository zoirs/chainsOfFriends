package ru.chernyshev.chainsOfFriends.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ru.chernyshev.chainsOfFriends.model.User;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OpenApiService {

    private final VkApiClient vk;
    private ServiceActor serviceActor;
    private final String uuid;

    @Autowired
    public OpenApiService(VkApiClient vk,
                          @Value("${client.id}") int clientId,
                          @Value("${client.secret}") String clientSecret,
                          @Value("${app.secret}") String appSecret) {
        this.vk = vk;
        this.serviceActor = new ServiceActor(clientId, clientSecret, appSecret);
        uuid = UUID.randomUUID().toString();
        System.out.println("constr uuid = " + uuid);
    }

    public User get(String id) {
        System.out.println("get uuid = " + uuid);

        List<UserXtrCounters> users = null;
        try {
            users = vk.users().get(serviceActor).userIds(id).fields(Fields.PHOTO_200_ORIG).execute();
        } catch (ApiException e) {
            return null;
        } catch (ClientException e) {
            return null;
        }
        return new User(users.get(0));
    }
}
