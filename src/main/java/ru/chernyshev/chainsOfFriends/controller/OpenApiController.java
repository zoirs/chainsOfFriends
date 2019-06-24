package ru.chernyshev.chainsOfFriends.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.chernyshev.chainsOfFriends.model.User;
import ru.chernyshev.chainsOfFriends.service.OpenApiService;

import java.util.List;

@RestController
public class OpenApiController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final OpenApiService openApiService;

    @Autowired
    public OpenApiController(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(String id) {
        if (StringUtils.isEmpty(id)) {
            logger.error("User {} not found ", id);
            return null;
        }
        logger.info("getUser " + id);
        List<User> users = openApiService.get(id);
        if (CollectionUtils.isEmpty(users)){
            logger.info("User {} not found ", id);
            return null;
        }
        return users.get(0);
    }

}
