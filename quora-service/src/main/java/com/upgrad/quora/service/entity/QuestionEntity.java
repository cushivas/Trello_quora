package com.upgrad.quora.service.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;


@Entity
@Table(name = "question")

@NamedQueries(
        {
                @NamedQuery(name = "getAllQuestions", query = "select u from QuestionEntity u"),
                @NamedQuery(name = "getQuestByUuid", query = "select u from QuestionEntity u where u.uuid=:uuid")
        }
)

public class QuestionEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private String uuid;

    @Column(name = "content")
    @Size(max = 500)
    private String content;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AnswerEntity answerList;

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public UserEntity getUser() {
        return user;
    }

    public AnswerEntity getAnswerList() {
        return answerList;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setAnswerList(AnswerEntity answerList) {
        this.answerList = answerList;
    }

}