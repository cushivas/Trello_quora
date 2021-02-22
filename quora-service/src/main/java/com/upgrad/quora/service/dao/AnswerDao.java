package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Dao implementation for Answer CRUD operations
 */
@Repository
public class AnswerDao {

    //@persistenceContext annotation is used to change the state of answerEntity from transient to persist
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Dao implementation to get answer by Uuid
     *
     * @param uuid
     * @return AnswerEntity
     */
    public AnswerEntity getAnswerByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("AnswerByUuid", AnswerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    /**
     * Dao implementation to create answer
     *
     * @param answerEntity
     * @return AnswerEntity
     */
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /**
     * Dao implementation to edit answer
     *
     * @param answerEntity
     * @return AnswerEntity
     */
    public AnswerEntity editAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
        return answerEntity;
    }


    /**
     * Dao implementation to delete an answer
     *
     * @param answerEntity
     * @return answer entity
     */

    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        entityManager.remove(answerEntity);
        return answerEntity;
    }


    /**
     * Dao implementation to retrieve all answers by question uuid
     *
     * @param questionId
     * @return
     */
    public List<AnswerEntity> getAllAnswersForQuestionById(int questionId) {
        return entityManager.createNamedQuery("getAllAnswersByQuestionId", AnswerEntity.class)
                .setParameter("question", questionId)
                .getResultList();
    }

}

