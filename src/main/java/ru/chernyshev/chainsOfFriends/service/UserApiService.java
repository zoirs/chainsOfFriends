package ru.chernyshev.chainsOfFriends.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserMin;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import ru.chernyshev.chainsOfFriends.FriendsGetQueryWithFieldsOverride;
import ru.chernyshev.chainsOfFriends.GetFieldsResponseOverride;
import ru.chernyshev.chainsOfFriends.UserX;
import ru.chernyshev.chainsOfFriends.model.SimpleChains;
import ru.chernyshev.chainsOfFriends.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserApiService {

    private static Logger logger = LoggerFactory.getLogger(UserApiService.class);

    private final VkApiClient vk;
    private UserActor actor;
    private UserXtrCounters user;

    @Autowired
    public UserApiService(VkApiClient vk) {
        this.vk = vk;
    }

    public SimpleChains search(int sourceUserId, int targetUserId) throws ApiException, ClientException {

        if (actor == null) {
            logger.warn("No actor");
            return null;
        }

        SimpleChains.Builder chainBuilder = new SimpleChains.Builder(String.valueOf(sourceUserId), String.valueOf(targetUserId));

        sleep();

        List<UserX> sourceUserFriends = getGetFriends(sourceUserId);
        List<UserX> targetUserFriends = getGetFriends(targetUserId);

        List<Integer> targetUserFriendIds = targetUserFriends.stream().map(UserMin::getId).collect(toList());
        List<Integer> sourceUserFriendsIds = sourceUserFriends.stream().map(UserMin::getId).collect(toList());

        if (targetUserFriendIds.contains(sourceUserId) || sourceUserFriendsIds.contains(targetUserId)) {
            logger.info("{} and {} are friend ", sourceUserId, targetUserId);
            chainBuilder.startChain().complete();
            return chainBuilder.build();
        }

        List<Integer> crossedFriends = sourceUserFriendsIds.stream().filter(targetUserFriendIds::contains).collect(toList());
        for (Integer crossedFriendId : crossedFriends) {
            chainBuilder.startChain()
                    .add(String.valueOf(crossedFriendId))
                    .complete();
        }
        if (chainBuilder.hasChain()) {
            logger.info("{} and {} has crossed friend", sourceUserId, targetUserId);
            return chainBuilder.build();
        }

        List<String> targetUserActiveFriends = getActiveFriends(targetUserFriends);
        List<String> sourceUserActiveFriends = getActiveFriends(sourceUserFriends);

        findMutual(Collections.singletonList(String.valueOf(targetUserId)),
                getSublist(sourceUserActiveFriends, 1),
                chainBuilder);

        if (chainBuilder.hasChain()) {
            logger.info("{} and {} has findMutual friend", sourceUserId, targetUserId);
            return chainBuilder.build();
        }

        findMutual(Collections.singletonList(String.valueOf(sourceUserId)),
                getSublist(targetUserActiveFriends, 1),
                chainBuilder);

        if (chainBuilder.hasChain()) {
            logger.info("{} and {} has findMutual friend", sourceUserId, targetUserId);
            return chainBuilder.build();
        }

        findMutual(
                getSublist(targetUserActiveFriends, sourceUserActiveFriends.size()),
                getSublist(sourceUserActiveFriends, targetUserActiveFriends.size()), chainBuilder);

        return chainBuilder.build();
    }

    private List<String> getActiveFriends(List<UserX> targetUserFriends) {
        return targetUserFriends.stream()
                .filter(userXtrLists -> StringUtils.isEmpty(userXtrLists.getDeactivated()) && (!userXtrLists.getClosed()))
                .map(userXtrLists -> String.valueOf(userXtrLists.getId()))
                .collect(toList());
    }

    private List<UserX> getGetFriends(int userId) throws ApiException, ClientException {
        GetFieldsResponseOverride userFriends = new FriendsGetQueryWithFieldsOverride(vk, actor, Fields.CITY, Fields.MAIDEN_NAME).userId(userId).execute();
        Integer friendsCount = userFriends.getCount();
        logger.info("У пользователя {} {} друзей", userId, friendsCount);
        return userFriends.getItems();
    }

    private List<String> getSublist(List<String> source, int targetSize) {
        return source.subList(0, getMaxIndex(source.size(), targetSize));
    }

    private void findMutual(List<String> targetUserActiveFriends, List<String> sourceUserActiveFriends, SimpleChains.Builder builder) throws ApiException, ClientException {
        //todo java 9 immutable list
        String e2 = Strings.join(targetUserActiveFriends, ',');

        String e1 = Strings.join(sourceUserActiveFriends, ',');

        sleep();

        // todo найти общих дрзей у u1 со всеми друзьями u2
        String procedure = "" +
                "var omk=[:e1],\n" +
                "i=[:e2],\n" +
                "jqw=omk.length,\n" +
                "puf,\n" +
                "tm=0,\n" +
                "shx=[];\n" +
                "while(tm<jqw) {\n" +
                "    puf=API.friends.getMutual({\"source_uid\":omk[tm],\"target_uids\":i});\n" +
                // todo возможно, тут можно отфильтровывать лишние
                // todo проверять количество элеметов в массиве shx, если слишком много, то можно заканчивать
//                "var i = 0;\n" +
//                "while (i < puf.length) {\n" +
//                "    i=i+1;\n" +
//                "    if (puf[i].common_count > 0) {     \n"+
//                "       shx.push({\"user\":omk[tm],\"f\":puf[i]});\n" +
//                "    }  \n"+
//                "}; \n"+

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

        logger.info("Результат: " + response.toString());

        JsonArray array = response.getAsJsonArray();

        for (int index = 0; index < targetUserActiveFriends.size(); index++) {
            JsonElement commonFriendsJson = array.get(index);
            JsonArray asJsonArray = commonFriendsJson.getAsJsonArray();
            for (JsonElement element : asJsonArray) {
//                  {
//                      "id": 5550613,
//                      "common_friends": [211805929],
//                      "common_count": 1
//                  },
                if (element.getAsJsonObject().get("common_count").getAsInt() > 0) {
                    String thirdId = element.getAsJsonObject().get("id").getAsString();
                    JsonArray common_friends = element.getAsJsonObject().get("common_friends").getAsJsonArray();
                    for (JsonElement secondFriend : common_friends) {
                        String firstId = targetUserActiveFriends.get(index);
                        builder.startChain()
                                .add(firstId)
                                .add(secondFriend.getAsString())
                                .add(thirdId)
                                .complete();

                    }
                }
            }
        }
    }

    private int getMaxIndex(int l1, int l2) {
        return l2 > l1 ? Math.min(l1, 20) : Math.min(100, l1);
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setActor(Integer userId, String accessToken) {
        logger.trace("Set actor for {}", userId);
        actor = new UserActor(userId, accessToken);
    }

    public UserActor getActor() {
        logger.trace("Get actor {}", actor);
        return actor;
    }

    public User getUser() {
        if (getActor() == null) {
            return null;
        }
        if (user != null) {
            return new User(user);
        }
        List<UserXtrCounters> users;
        try {
            users = vk.users().get(actor).userIds(String.valueOf(actor.getId())).fields(Fields.PHOTO_200_ORIG).execute();
        } catch (ApiException | ClientException e) {
            return null;
        }
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        user = users.get(0);
        return new User(user);
    }
}
