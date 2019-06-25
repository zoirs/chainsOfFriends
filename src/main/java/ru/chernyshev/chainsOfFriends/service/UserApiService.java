package ru.chernyshev.chainsOfFriends.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.MutualFriend;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserMin;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.friends.FriendsGetMutualQueryWithTargetUids;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import ru.chernyshev.chainsOfFriends.FriendsGetQueryWithFieldsOverride;
import ru.chernyshev.chainsOfFriends.GetFieldsResponseOverride;
import ru.chernyshev.chainsOfFriends.UserX;
import ru.chernyshev.chainsOfFriends.model.SimpleChains;
import ru.chernyshev.chainsOfFriends.model.User;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserApiService {

    private static Logger logger = LoggerFactory.getLogger(UserApiService.class);

    private static final int SECOND = 1_000;

    private final VkApiClient vk;
    private UserActor actor;
    private UserXtrCounters user;

    @Autowired
    public UserApiService(VkApiClient vk) {
        logger.info("Initialize UserApiService");
        this.vk = vk;
    }

    public void setActor(Integer userId, String accessToken) {
        logger.info("Set actor for {}", userId);
        actor = new UserActor(userId, accessToken);
    }

    public boolean needAuth() {
        boolean isAuthorized = actor != null;
        logger.info("Is authorized - {}", isAuthorized);
        return !isAuthorized;
    }

    public User getUser() {
        if (needAuth()) {
            logger.warn("Not authorized user");
            return null;
        }
        if (user != null) {
            return new User(user);
        }
        String actorId = String.valueOf(actor.getId());
        List<UserXtrCounters> users;
        try {
            users = vk.users().get(actor).userIds(actorId).fields(Fields.PHOTO_200_ORIG).execute();
        } catch (ApiException | ClientException e) {
            logger.error("Vk api error. Can't get authorized user", e);
            return null;
        }
        if (CollectionUtils.isEmpty(users)) {
            logger.warn("Can't get actor user {}", actorId);
            return null;
        }
        user = users.get(0);
        return new User(user);
    }

    public SimpleChains search(int sourceUserId, int targetUserId) throws ApiException, ClientException {

        if (actor == null) {
            logger.warn("No actor");
            return null;
        }

        SimpleChains.Builder chainBuilder = new SimpleChains.Builder(sourceUserId, targetUserId);

        long lastReqTime = new Date().getTime();

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
                    .add(crossedFriendId)
                    .complete();
        }
        if (chainBuilder.hasChain()) {
            logger.info("{} and {} has crossed friend", sourceUserId, targetUserId);
            return chainBuilder.build();
        }

        List<Integer> targetUserActiveFriends = filterActiveFriends(targetUserFriends);
        List<Integer> sourceUserActiveFriends = filterActiveFriends(sourceUserFriends);

        if (CollectionUtils.isEmpty(targetUserActiveFriends) || CollectionUtils.isEmpty(sourceUserActiveFriends)) {
            logger.info("No friend");
            return chainBuilder.build();
        }

//        janyleb
//        belov.live id140891700

        sleepIfNeed(lastReqTime);
        lastReqTime = new Date().getTime();

        getMutualFriends(targetUserId,
                getSublist(sourceUserActiveFriends, 1),
                chainBuilder, false);

        if (chainBuilder.hasChain()) {
            logger.info("First step {} and {} has findMutual friend", sourceUserId, targetUserId);
            return chainBuilder.build();
        }

        getMutualFriends(sourceUserId,
                getSublist(targetUserActiveFriends, 1),
                chainBuilder, true);

        if (chainBuilder.hasChain()) {
            logger.info("Second step {} and {} has findMutual friend", sourceUserId, targetUserId);
            return chainBuilder.build();
        }

        sleepIfNeed(lastReqTime);

        findMutual(
                getSublist(sourceUserActiveFriends, targetUserActiveFriends.size()),
                getSublist(targetUserActiveFriends, sourceUserActiveFriends.size()), chainBuilder);

        return chainBuilder.build();
    }

    private void sleepIfNeed(long firstReqTime) {
        long currentTime = new Date().getTime();
        long millis = currentTime - firstReqTime;
        if (millis < SECOND) {
            long sleepTime = SECOND - millis;
            logger.info("Sleep time {}", sleepTime);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.warn("Sleep exc", e);
            }
        }
    }

    private List<Integer> filterActiveFriends(List<UserX> targetUserFriends) {
        return targetUserFriends.stream()
                .filter(userXtrLists -> StringUtils.isEmpty(userXtrLists.getDeactivated()) && (!userXtrLists.getClosed()))
                .map(UserMin::getId)
                .collect(toList());
    }

    private List<UserX> getGetFriends(int userId) throws ApiException, ClientException {//todo поля вероятно лишние
        GetFieldsResponseOverride userFriends = new FriendsGetQueryWithFieldsOverride(vk, actor, Fields.CITY, Fields.MAIDEN_NAME).userId(userId).execute();
        Integer friendsCount = userFriends.getCount();
        logger.info("У пользователя {} {} друзей", userId, friendsCount);
        return userFriends.getItems();
    }

    private void getMutualFriends(int userId, List<Integer> targetUserIds, SimpleChains.Builder builder, boolean isRevert) throws ApiException, ClientException {
        List<MutualFriend> mutualFriends = new FriendsGetMutualQueryWithTargetUids(vk, actor, targetUserIds).sourceUid(userId).execute();
        mutualFriends.stream()
                .filter(m -> m.getCommonCount() > 0)
                .forEach(mutualFriend -> {
                            mutualFriend.getCommonFriends().forEach(commonFriendId -> {
                                if (isRevert) {
                                    builder.startChain()
                                            .add(commonFriendId)
                                            .add(mutualFriend.getId())
                                            .complete();
                                } else {
                                    builder.startChain()
                                            .add(mutualFriend.getId())
                                            .add(commonFriendId)
                                            .complete();
                                }
                            });
                        }
                );
    }

    private <T> List<T> getSublist(List<T> source, int targetSize) {
        return source.subList(0, getMaxIndex(source.size(), targetSize));
    }

    private void findMutual(List<Integer> targetUserFriends, List<Integer> sourceUserFriends, SimpleChains.Builder builder) throws ApiException, ClientException {
        //todo java 9 immutable list
        String e2 = Strings.join(targetUserFriends, ',');

        String e1 = Strings.join(sourceUserFriends, ',');

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
                "    shx.push(puf);\n" +
                "    tm=tm+1;\n" +
                "}\n" +
                "return shx;";

        List<Integer> list;
        boolean isReverted = e2.length() < e1.length();
        if (isReverted) {
            list = targetUserFriends;
            procedure = procedure.replace(":e1", e2);
            procedure = procedure.replace(":e2", e1);
        } else {
            list = sourceUserFriends;
            procedure = procedure.replace(":e1", e1);
            procedure = procedure.replace(":e2", e2);
        }

        logger.info("Call procedure {}", procedure);

        JsonElement response = vk.execute().code(actor, procedure)
                .execute();

        logger.info("Result procedure " + response.toString());

        JsonArray array = response.getAsJsonArray();

        for (int index = 0; index < list.size(); index++) {
            JsonElement commonFriendsJson = array.get(index);
            JsonArray asJsonArray = commonFriendsJson.getAsJsonArray();
            for (JsonElement element : asJsonArray) {
//                  {
//                      "id": 5550613,
//                      "common_friends": [211805929],
//                      "common_count": 1
//                  },
                if (element.getAsJsonObject().get("common_count").getAsInt() > 0) {
                    Integer thirdId = element.getAsJsonObject().get("id").getAsInt();
                    JsonArray common_friends = element.getAsJsonObject().get("common_friends").getAsJsonArray();
                    for (JsonElement secondFriend : common_friends) {
                        Integer firstId = list.get(index);
                        if (!isReverted) {
                            builder.startChain()
                                    .add(thirdId)
                                    .add(secondFriend.getAsInt())
                                    .add(firstId)
                                    .complete();

                        } else {
                            builder.startChain()
                                    .add(firstId)
                                    .add(secondFriend.getAsInt())
                                    .add(thirdId)
                                    .complete();

                        }
                    }
                }
            }
        }
    }

    private int getMaxIndex(int l1, int l2) {
        return l2 > l1 ? Math.min(l1, 20) : Math.min(100, l1);
    }

}
