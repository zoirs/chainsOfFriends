package ru.chernyshev.chainsOfFriends.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.chernyshev.chainsOfFriends.model.User;
import ru.chernyshev.chainsOfFriends.service.OpenApiService;

@RestController
public class OpenApiController {

    private final OpenApiService openApiService;

    @Autowired
    public OpenApiController(OpenApiService openApiService) {
        this.openApiService = openApiService;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(String id) {
        System.out.println("======== getUser =======" + id);
        return openApiService.get(id).get(0);
    }

}
