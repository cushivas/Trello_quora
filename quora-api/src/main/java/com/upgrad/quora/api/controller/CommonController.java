package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
/**
 * API of Common Services
 */
public class CommonController {

    @Autowired
    private CommonService commonService;

    /**
     * Method create API getUser endpoint
     *
     * @param authorizationToken
     * @param userUuid
     * @return UserDetailResponse with Http status
     * @throws UserNotFoundException        if the authorization details of user are not found
     * @throws AuthorizationFailedException if user is not authorised to get details of user
     */

    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("userId") final String userUuid, @RequestHeader("authorization") final String authorizationToken)
            throws UserNotFoundException, AuthorizationFailedException {
        final UserEntity userEntity = commonService.getUser(userUuid, authorizationToken);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(userEntity.getFirstName()).lastName(userEntity.getLastName()).userName(userEntity.getUsername()).emailAddress(userEntity.getEmail()).country(userEntity.getCountry()).aboutMe(userEntity.getAboutMe()).contactNumber(userEntity.getContactNumber()).dob(userEntity.getContactNumber());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }

}
