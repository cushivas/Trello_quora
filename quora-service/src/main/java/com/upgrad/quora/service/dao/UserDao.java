package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.Entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    //@persistenceContext annotation is used to change the state of userEntity from transient to persist
    @PersistenceContext
    private EntityManager entityManager;


    //The getUserByUsername() method is used to check if there  already exist a user with same email
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            System.out.println(nre.getMessage());
            return null;
        }
    }

    //The getUserByUsername() method is used to check if there  already exist a user with same username
    public UserEntity getUserByUsername(final String username) {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            System.out.println(nre.getMessage());
            return null;
        }
    }


    //This method is used to register a user in data base by storing all the user information
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }


}
