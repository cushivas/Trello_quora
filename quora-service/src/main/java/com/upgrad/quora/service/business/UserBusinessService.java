package com.upgrad.quora.service.business;

import com.upgrad.quora.service.Entity.UserEntity;
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

    //The signup method is used to register a user by calling the createUser() method of UserDao class
    //This method also generate SignUPRestrictedException to check if the username or email already present in database
    //It gives the error message if the username or email has already taken or used by some other user and if not it register the user
    //This method also encrypt the password and set password and generate salt in encrypted format
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

        UserEntity existingUserEntityUsername = userDao.getUserByUsername(userEntity.getUsername());
        if (existingUserEntityUsername != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }

        UserEntity existingUserEntityEmail = userDao.getUserByEmail((userEntity.getEmail()));
        if (existingUserEntityEmail != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }

        String password = userEntity.getPassword();
        if (password == null) {
            throw new SignUpRestrictedException("SGR-003", "Password cannot be null");
        } else {

            String[] encryptedText = PasswordCryptographyProvider.encrypt(userEntity.getPassword());
            userEntity.setSalt(encryptedText[0]);
            userEntity.setPassword(encryptedText[1]);
        }
        return userDao.createUser(userEntity);
    }
}
