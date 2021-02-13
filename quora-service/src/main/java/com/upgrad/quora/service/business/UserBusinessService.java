package com.upgrad.quora.service.business;

import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    /**
     * The signup method is used to register a user
     *
     * @param userEntity object of UserEntity class
     * @return userEntity object
     * @throws SignUpRestrictedException if validation requirement are not followed
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

        //Check if the username already available in database
        UserEntity existingUserEntityUsername = userDao.getUserByUsername(userEntity.getUsername());
        if (existingUserEntityUsername != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }

        //Check if Email already exist
        UserEntity existingUserEntityEmail = userDao.getUserByEmail((userEntity.getEmail()));
        if (existingUserEntityEmail != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }

        //Check if Password is null
        String password = userEntity.getPassword();

        if (password == null) {
            throw new SignUpRestrictedException("SGR-003", "Password cannot be null");
        } else {
            //Password and Salt are Encrypted
            String[] encryptedText = PasswordCryptographyProvider.encrypt(userEntity.getPassword());
            userEntity.setSalt(encryptedText[0]);
            userEntity.setPassword(encryptedText[1]);
        }
        return userDao.createUser(userEntity);
    }

}

