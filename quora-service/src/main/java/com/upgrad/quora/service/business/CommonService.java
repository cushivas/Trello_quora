package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonService {

    @Autowired
    private UserDao userDao;

    /**
     * Service implementation for get user endpoint
     * @param userUuid for getting all details of user
     * @param accessToken for validation
     * @return userEntityByUuid that is the details of user
     * @throws UserNotFoundException if user uuid is not found in database
     * @throws AuthorizationFailedException if authorization details are invalid
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getUser(final String userUuid, final String accessToken) throws UserNotFoundException, AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserByAccessToken(accessToken);

        //Check if accessToken enter by user exist in database
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Check if user has signOut
        if (userAuthTokenEntity.getLogoutAt() != null && userAuthTokenEntity.getLogoutAt().isAfter(userAuthTokenEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }

        //Check if uuid  exist in database
        UserEntity userEntityByUuid = userDao.getUserByUuid(userUuid);
        if (userEntityByUuid == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }
        return userEntityByUuid;
    }

}
