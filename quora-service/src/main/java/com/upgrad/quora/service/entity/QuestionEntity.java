package com.upgrad.quora.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Table(name = "question")

@NamedQueries(
        {
                @NamedQuery(name = "getAllQuestions", query = "select u from QuestionEntity u"),
                @NamedQuery(name = "getQuestionByUuid", query = "select u from QuestionEntity u where u.uuid = :uuid")
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

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AnswerEntity> answerList = new ArrayList();

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

    public List<AnswerEntity> getAnswerList() {
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

    public void setAnswerList(List<AnswerEntity> answerList) {
        this.answerList = answerList;
    }

}