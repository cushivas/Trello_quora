package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

/**
 * Dao implementation for Question CRUD Operations
 */

@Repository
public class QuestionDao {

    //@persistenceContext annotation is used to change the state of questionEntity from transient to persist
    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Dao implementation to create Questions
     *
     * @param questionEntity
     * @return question entity
     */
    public QuestionEntity createQuestionForUser(QuestionEntity questionEntity) {

        entityManager.persist(questionEntity);
        return questionEntity;
    }


    /**
     * Dao implementation to retrieve all Questions
     *
     * @return list of Questions
     */
    public List<QuestionEntity> getAllQuestions() {

        return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
    }


    /**
     * Dao Implementation to edit Questions
     *
     * @param questionEntity containing Question details
     * @return updated QuestionEntity
     */
    public QuestionEntity editQuestion(QuestionEntity questionEntity) {

        entityManager.merge(questionEntity);
        return questionEntity;
    }


    /**
     * Dao Implementation to delete Question
     *
     * @param questionEntity which is to be deleted
     * @return question entity
     */
    public QuestionEntity deleteQuestion(QuestionEntity questionEntity) {

        entityManager.remove(questionEntity);
        return questionEntity;
    }

    /**
     * Dao implementation to get Question by uuid
     *
     * @param uuid
     * @return Question entity
     */

    public QuestionEntity getQuestionByUuid(String uuid) {

        try {
            return entityManager.createNamedQuery("getQuestByUuid", QuestionEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

}