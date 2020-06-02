package com.appsdeveloperblog.app.ws.controller;

import com.appsdeveloperblog.app.ws.request.UserDetailsRequstModel;
import com.appsdeveloperblog.app.ws.request.UpdateUserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.model.UserRest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {
    Map<String, UserRest> users;


    @GetMapping()
    public String getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "50") int limit,
                           @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort) {
        return "get users was called with page = " + page + " and limit = " + limit + " and sort = " + sort;
    }

    @GetMapping(path = "/{userId}",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<UserRest> getUser(@PathVariable String userId) {
        if(users.containsKey(userId)) {

            return new ResponseEntity<>(users.get(userId), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(consumes = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    }, produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserDetailsRequstModel userDetails) {

        UserRest userRest = new UserRest();
        userRest.setEmail(userDetails.getEmail());
        userRest.setFirstName(userDetails.getFirstName());
        userRest.setLastName(userDetails.getLastName());
        String userId = UUID.randomUUID().toString();
        userRest.setUserId(userId);

        if (users == null) users = new HashMap<>();
        users.put(userId, userRest);



        return new ResponseEntity<>(userRest, HttpStatus.OK);
    }


    @PutMapping(path = "/{userId}", consumes = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    }, produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public UserRest updateUser(@PathVariable String userId, @RequestBody UpdateUserDetailsRequestModel userDetails) {

        UserRest storedUserDetails = users.get(userId);
        storedUserDetails.setFirstName(userDetails.getFirstName());
        storedUserDetails.setLastName(userDetails.getLastName());


        users.put(userId, storedUserDetails);

        return storedUserDetails;
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {
        users.remove(userId);

        return ResponseEntity.noContent().build();
    }

}
