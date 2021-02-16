package com.upgrad.quora.service.dao;

//import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    //@persistenceContext annotation is used to change the state of userEntity from transient to persist
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The getUserByUsername() method is used to check email entered by user
     *
     * @param email which is saved by user in request model userEntity
     * @return userEntity object if user email exist in database
     */
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            System.out.println(nre.getMessage());
            return null;
        }
    }

    /**
     * The getUserByUsername() method is used to check username entered by user
     *
     * @param username added by user in request model userEntity
     * @return userEntity object if username already exist in data base
     */
    public UserEntity getUserByUsername(final String username) {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            System.out.println(nre.getMessage());
            return null;
        }
    }

    /**
     *This method gets AuthToken from database
     * @param accesstoken
     * @return
     */
    public UserAuthTokenEntity getUserAuthToken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method is used to register a user in data base by storing all the user information
     *
     * @param userEntity object
     * @return userEntity
     */
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    /**
     * This method is used to store authentication token in the Database
     * @param userAuthTokenEntity
     * @return
     */
    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    /**
     * This method is used to update the token in the database
     * @param updatedUserEntity
     */
    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    /**
     * This method is used to save the AuthToken
     * @param userAuthTokenEntity
     * @return
     */
    public UserAuthTokenEntity saveAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.merge(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

}
