package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Handling AnswerEntity and respective CRUD operations
 */

@Service
public class AnswerBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;

    /**
     * Service class implementation of createAnswer utility
     *
     * @param questionUuid whose answer is to be created
     * @param answerEntity which will contaion answer details
     * @param accessToken for validation
     * @return answer entity
     * @throws InvalidQuestionException if question id is invalid
     * @throws AuthorizationFailedException if validation details are not as desired
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswers(String questionUuid, AnswerEntity answerEntity, String accessToken) throws InvalidQuestionException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserByAccessToken(accessToken);

        //Check if Access token enter by user  exist in database or not
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Check if user has signed out

        if (userAuthTokenEntity.getLogoutAt() != null && userAuthTokenEntity.getLogoutAt().isAfter(userAuthTokenEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
        }
        answerEntity.setUser(userAuthTokenEntity.getUser());
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionUuid);

        //Check if the question uuid entered by the user whose answer is to be posted does not exist in the database
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        answerEntity.setDate(LocalDateTime.now());
        answerEntity.setQuestion(questionEntity);
        answerEntity.setUser(userAuthTokenEntity.getUser());

        return answerDao.createAnswer(answerEntity);

    }

    /**
     * Service class implementation of edit answer by answerUuid if valid..
     *
     * @param answerUuid which is to be edited
     * @param content previous detail of answer
     * @param accessToken for validation
     * @return updated answer entity
     * @throws AuthorizationFailedException if validation fails
     * @throws AnswerNotFoundException if answer uuid not found
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(String answerUuid, String content, String accessToken) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

        //Check if the access token provided by the user does not exist in the database
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Check if the user has signed out
        if (userAuthTokenEntity.getLogoutAt() != null && userAuthTokenEntity.getLogoutAt().isAfter(userAuthTokenEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
        }
        AnswerEntity answerEntity = answerDao.getAnswerByUuid(answerUuid);

        // Check if the answer with uuid which is to be edited does not exist in the database
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }

        // Check if the non owner of answer tries to edit it
        if (!answerEntity.getUser().getUuid().equals(userAuthTokenEntity.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        answerEntity.setDate(LocalDateTime.now());
        answerEntity.setAns(content);
        return answerDao.editAnswer(answerEntity);

    }

    /**
     * Service class implementation of delete answer if answerUuid is a valid one.
     *
     * @param answerUuid which is to be deleted
     * @param accessToken for validation
     * @return answer entity
     * @throws AuthorizationFailedException if authorization fails
     * @throws AnswerNotFoundException if answer uuid not found
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(String answerUuid, String accessToken) throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

        //Check if the access token provided by the user does not exist in the database
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Check if the user has signed out
        if (userAuthTokenEntity.getLogoutAt() != null && userAuthTokenEntity.getLogoutAt().isAfter(userAuthTokenEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete an answer");
        }

        AnswerEntity answerEntity = answerDao.getAnswerByUuid(answerUuid);

        //Check if the answer with uuid which is to be deleted does not exist in the database
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }

        //Check if non owner user of the answer tries to delete the answer
        if ((!answerEntity.getUser().getUuid().equals(userAuthTokenEntity.getUser().getUuid())) || (!answerEntity.getUser().getRole().equals("nonadmin"))) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
        return answerDao.deleteAnswer(answerEntity);
    }

    /**
     * Service implementation for getting all answers for a question by question uuid
     *
     * @param questionUuid whose answer are to be retrieve
     * @param token for validtion
     * @return list of all answers
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException if id is invalid
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAllAnswersForQuestion(String questionUuid, String token) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(token);

        //Check if the access token provided by the user does not exist in the database
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Check if the user has signed out
        if (userAuthTokenEntity.getLogoutAt() != null && userAuthTokenEntity.getLogoutAt().isAfter(userAuthTokenEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the answers");
        }

        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionUuid);
        //Check if the question with uuid whose answers are to be retrieved from the database does not exist
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }


        List<AnswerEntity> answerEntityList = answerDao.getAllAnswersForQuestionById(questionEntity.getId());
        return answerEntityList;

    }

}
