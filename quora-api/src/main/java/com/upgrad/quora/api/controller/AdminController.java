package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.UserAdminService;
import com.upgrad.quora.service.business.UserBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.quora.service.exception.*;

@RestController
@RequestMapping("/")

/**
 *  Admin controller for all admin functionalities on user
 *  ex: user delete
 *  @author Ashish Shivhare
 */
public class AdminController {

    @Autowired
    private UserAdminService userAdminService;

    /**
     *  Delete User controller for deleting user by userUuid
     * @param userId
     * @param authorizationToken
     * @return
     * @throws UserNotFoundException
     * @throws AuthorizationFailedException
     * @author Ashish Shivhare
     */

    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorizationToken)  throws UserNotFoundException, AuthorizationFailedException {
        final String deletedUserUuid = this.userAdminService.deleteUser(userId, authorizationToken);
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(deletedUserUuid).status("USER SUCCESSFULLY DELETED");
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, headers, HttpStatus.OK);
    }


}
