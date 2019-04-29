package ru.chernyshev.chainsOfFriends;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChainsOfFriendsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChainsOfFriendsApplication.class, args);
    }

    @Bean
    public VkApiClient vk() {
        return new VkApiClient(new HttpTransportClient());
    }

}
