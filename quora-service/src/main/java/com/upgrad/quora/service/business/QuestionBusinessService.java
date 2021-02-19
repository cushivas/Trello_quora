package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author swapnadeep.dutta
 */

@Service
public class QuestionBusinessService {

    @Autowired
    QuestionDao questionDao;

    @Autowired
    UserDao userDao;

    /**
     * The method is used to create the service layer to create question by the user
     *
     * @param questionEntity
     * @param token
     * @return
     * @throws AuthorizationFailedException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestionForUser(QuestionEntity questionEntity, String token)
            throws AuthorizationFailedException {

        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(token);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userAuthEntity.getLogoutAt() != null && userAuthEntity.getLogoutAt().isAfter(userAuthEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }

        questionEntity.setUserEntity(userAuthEntity.getUser());
        questionDao.createQuestionForUser(questionEntity);

        return questionEntity;
    }

    /**
     * The method is used to create the service layer to get all questions from the list
     *
     * @param token
     * @return
     * @throws AuthorizationFailedException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(String token) throws AuthorizationFailedException {

        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(token);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userAuthEntity.getLogoutAt() != null && userAuthEntity.getLogoutAt().isAfter(userAuthEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }

        List<QuestionEntity> questionEntityList = questionDao.getAllQuestions();

        return questionEntityList;
    }

    /**
     * The method is used to create the service layer to update the question
     *
     * @param questionUuid
     * @param token
     * @param questionEntity
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity updateQuestion(String questionUuid, String token, QuestionEntity questionEntity)
            throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(token);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userAuthEntity.getLogoutAt() != null && userAuthEntity.getLogoutAt().isAfter(userAuthEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }

        QuestionEntity questionEntity1 = questionDao.getQuestionByUuid(questionUuid);

        if (questionEntity1 == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        if (!questionEntity1.getUserEntity().getUuid().equals(userAuthEntity.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }

        questionEntity1.setContent(questionEntity.getContent());

        return questionDao.updateQuestion(questionEntity1);
    }

    /**
     * The method is used to create the service layer to delete the question
     *
     * @param uuid
     * @param token
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(String uuid, String token)
            throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(token);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userAuthEntity.getLogoutAt() != null && userAuthEntity.getLogoutAt().isAfter(userAuthEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002",
                    "User is signed out.Sign in first to delete the question");
        }

        QuestionEntity questionEntity = questionDao.getQuestionByUuid(uuid);

        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        if (!questionEntity.getUserEntity().getUuid().equals(userAuthEntity.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003",
                    "Only the question owner or admin can delete the question");
        }

        return questionEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsByUser(String uuid, String token)
            throws AuthorizationFailedException, UserNotFoundException {

        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(token);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userAuthEntity.getLogoutAt() != null && userAuthEntity.getLogoutAt().isAfter(userAuthEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }

        UserEntity userEntity = userDao.getUserByUuid(uuid);

        if (userEntity == null) {
            throw new UserNotFoundException("USR-001",
                    "User with entered uuid whose question details are to be seen does not exist");
        }

        return userEntity.getQuestionEntityList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity getQuestionBuUuid(String uuid) {
        return questionDao.getQuestionByUuid(uuid);
    }
}
