package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author swapnadeep.dutta
 *
 */

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The method is used whenever the user tries to create a question
     *
     * @param questionEntity
     * @return
     */

    public QuestionEntity createQuestionForUser(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /**
     * The method is used to fetch all the question posted by the user
     *
     * @return
     */

    public List<QuestionEntity> getAllQuestions() {
        return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
    }

    /**
     * The method is used to get the questions posted by user by UUID
     *
     * @param uuid
     * @return
     */

    public QuestionEntity getQuestionByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("getQuestionByUuid", QuestionEntity.class).setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    /**
     * The method is used to update the question posted by user
     *
     * @param questionEntity
     * @return
     */

    public QuestionEntity updateQuestion(QuestionEntity questionEntity) {
        // TODO Auto-generated method stub
        entityManager.merge(questionEntity);
        return questionEntity;
    }

    /**
     * The method is used to delete the question posted by user
     *
     * @param questionEntity
     * @return
     */

    public QuestionEntity deleteQuestion(QuestionEntity questionEntity) {
        // TODO Auto-generated method stub
        entityManager.remove(questionEntity);
        return questionEntity;
    }

}
