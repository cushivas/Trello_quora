package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author swapnadeep.dutta
 *
 */

@Entity
@Table(name = "question")

/**
 *
 * JPQL queries to fetch the data from database
 *
 */

@NamedQueries({ @NamedQuery(name = "getQuestionByUuid", query = "select  u from QuestionEntity u where u.uuid =:uuid"),
        @NamedQuery(name = "getAllQuestions", query = "select u from QuestionEntity u ") })

public class QuestionEntity implements Serializable {

    // Entities to be used for developing Questions End-points

    /**
     * Defaulted serial version uuid
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "uuid")
    @Size(max = 200)
    private String uuid;

    @Column(name = "content")
    @Size(max = 500)
    private String content;

    @NotNull
    @Column(name = "date")
    LocalDateTime date;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AnswerEntity answerEntity;

    /**
     * Getters and Setters to persist and fetch from the database
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public AnswerEntity getAnswerEntity() {
        return answerEntity;
    }

    public void setAnswerEntity(AnswerEntity answerEntity) {
        this.answerEntity = answerEntity;
    }

}
