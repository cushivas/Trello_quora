package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
/**
 * API of Question Services
 */
public class QuestionController {


    @Autowired
    private QuestionBusinessService questionBusinessService;

    /**
     * Create Question  method for posting new Questions
     *
     * @param questionRequest
     * @param authorization
     * @return Newly created Question entity
     * @throws AuthorizationFailedException
     */


    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setDate(LocalDateTime.now());
        questionEntity.setUuid(UUID.randomUUID().toString());
        QuestionEntity createdQuestion = questionBusinessService.createQuestionForUser(questionEntity, authorization);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

    }

    /**
     * Edit Question method used to let user having required access to edit the question
     *
     * @param questionEditRequest
     * @param authorization
     * @return Updated Question Entity
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{question_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(@PathVariable(value = "question_id") final String questionId, final QuestionEditRequest questionEditRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionEditRequest.getContent());
        questionEntity.setDate(LocalDateTime.now());
        QuestionEntity editedQuestion = questionBusinessService.editQuestion(questionId, questionEntity, authorization);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(editedQuestion.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);

    }

    /**
     * Method to retrieve all the questions By any user
     *
     * @param authorization
     * @return List of all the Questions
     * @throws AuthorizationFailedException
     */

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        List<QuestionEntity> questionEntities = questionBusinessService.getAllQuestions(authorization);
        //New List is created to store and return the list of all the Questions
        List<QuestionDetailsResponse> questionDetailsResponseList = new LinkedList<>();


        for (QuestionEntity questionEntity : questionEntities) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(questionEntity.getUuid()).content(questionEntity.getContent());
            questionDetailsResponseList.add(questionDetailsResponse);
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);

    }

    /**
     * This method is used to fetch all the questions posted by a specific user
     *
     * @param uuid
     * @param authorization
     * @return List of questions
     * @throws UserNotFoundException
     * @throws AuthorizationFailedException
     */

    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable(value = "userId") final String uuid, @RequestHeader(value = "authorization") final String authorization) throws UserNotFoundException, AuthorizationFailedException {

        List<QuestionEntity> questionEntities = questionBusinessService.getAllQuestionByUser(uuid, authorization);
        //New List is created to store and return the list of all the Questions
        List<QuestionDetailsResponse> questionDetailsResponseList = new LinkedList<>();


        for (QuestionEntity questionEntity : questionEntities) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(questionEntity.getUuid()).content(questionEntity.getContent());
            questionDetailsResponseList.add(questionDetailsResponse);
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);

    }

    /**
     * This method  is used to delete a question that has been posted by a user
     *
     * @param questionId
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */

    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable(value = "questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity deletedQuestion = questionBusinessService.deleteQuestion(questionId, authorization);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(deletedQuestion.getUuid()).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

}