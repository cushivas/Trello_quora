package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class implementation for CRUD operation on QuestionEntity
 */

@Service
public class QuestionBusinessService {

    @Autowired
    QuestionDao questionDao;

    @Autowired
    UserDao userDao;

    /**
     * Service class implementation for creating a question,
     * This method takes questionEntity as input and creates a question
     * @param questionEntity for storing content
     * @param token for validation
     * @return QuestionEntity of newly created question
     * @throws AuthorizationFailedException if validations conditions conflict
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestionForUser(QuestionEntity questionEntity, String token) throws AuthorizationFailedException {

        UserAuthTokenEntity userAuth = userDao.getUserAuthToken(token);
        //Check if the access token provided by the user does not exist in the database
        if (userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Check if the user has signed out
        if (userAuth.getLogoutAt() != null && userAuth.getLogoutAt().isAfter(userAuth.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }

        questionEntity.setUser(userAuth.getUser());

        questionDao.createQuestionForUser(questionEntity);
        return questionEntity;

    }

    /**
     * Service class implementation for getting all question
     * This method takes authentication token as input and returns list of all questions
     * @param token to check if token is available
     * @return List of question entity object containing all questions
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(String token) throws AuthorizationFailedException {

        UserAuthTokenEntity userAuth = userDao.getUserAuthToken(token);
        //Check if access token provided by user exist in data base
        if (userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        //Check if user has signed out
        if (userAuth.getLogoutAt() != null && userAuth.getLogoutAt().isAfter(userAuth.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }

        List<QuestionEntity> questionEntityList = questionDao.getAllQuestions();
        return questionEntityList;

    }

    /**
     * Service class implementation for editing a question
     *
     * @param questUuid The question id which is to be edited
     * @param questionEntity The question to be edited
     * @param token  to check if token is available
     * @return QuestionEntity
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(String questUuid, QuestionEntity questionEntity, String token) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuth = userDao.getUserAuthToken(token);
        //Check if access token exist in database
        if (userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Check if user has sign out
        if (userAuth.getLogoutAt() != null && userAuth.getLogoutAt().isAfter(userAuth.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }

        QuestionEntity currentQuestionEntity = questionDao.getQuestionByUuid(questUuid);
        //Check if  required Question uuid exist in database
        if (currentQuestionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        //Check if current user is the owner of requested question
        if (!currentQuestionEntity.getUser().getUuid().equals(userAuth.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        currentQuestionEntity.setContent(questionEntity.getContent());
        return questionDao.editQuestion(currentQuestionEntity);

    }

    /**
     * Service class implementation for deleting a question by questionUuid
     * This method takes question uuid as input and deletes respective question from db
     * @param questUuid The question id which is to be deleted
     * @param token for validating authorization
     * @return QuestionEntity
     * @throws AuthorizationFailedException if authorization fails
     * @throws InvalidQuestionException   if question id is not valid
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(String questUuid, String token) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuth = userDao.getUserAuthToken(token);

        // Check if user access token exist in database or not
        if (userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Check if user has signed out
        if (userAuth.getLogoutAt() != null && userAuth.getLogoutAt().isAfter(userAuth.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete the question");
        }

        //Check if question uuid exist in database or not
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questUuid);

        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        //Check if user is owner of question
        if (!questionEntity.getUser().getUuid().equals(userAuth.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
        return questionDao.deleteQuestion(questionEntity);

    }

    /**
     * Service class implementation for retrieving all questions for a user
     * by userUuid..
     *
     * @param userUuid get all Question related to this id
     * @param token validating access
     * @return List of all Questions
     * @throws AuthorizationFailedException if authorization conditions fail
     * @throws UserNotFoundException if id not found
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionByUser(String userUuid, String token) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthTokenEntity userAuth = userDao.getUserAuthToken(token);

        //check if access token exist or not
        if (userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Check if user has signed out
        if (userAuth.getLogoutAt() != null && userAuth.getLogoutAt().isAfter(userAuth.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }

        // Check if user uuid exist or not
        UserEntity userEntity = userDao.getUserByUuid(userUuid);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return userEntity.getQuestionList();

    }



}