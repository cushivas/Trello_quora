package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.upgrad.quora.service.exception.*;

@Service
public class UserAdminService {

    @Autowired
    private UserDao userDao;


    /**
     * Service class for user delete if user is having admin role
     *
     * @param userId      the id of user which is to be deleted
     * @param accessToken for  validation
     * @throws UserNotFoundException  if user id not found
     * @throws AuthorizationFailedException violation of validation
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteUser(final String userId, final String accessToken) throws UserNotFoundException, AuthorizationFailedException {

        final UserAuthTokenEntity userAuthTokenEntity = userDao.getUserByAccessToken(accessToken);

        //Check if accessToken enter by user exist in database
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Check if user has signOut
        if (userAuthTokenEntity.getLogoutAt() != null && userAuthTokenEntity.getLogoutAt().isAfter(userAuthTokenEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        }

        //Check if uuid exist in database
        final UserEntity userEntityByUuid = userDao.getUserByUuid(userId);
        if (userEntityByUuid == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }

        // Check if user has role other than admin..
        // if nonadmin throw exception and block the deletion..
        if (userAuthTokenEntity.getUser().getRole().equals("nonadmin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }

        return userDao.deleteUser(userEntityByUuid);
    }


}
