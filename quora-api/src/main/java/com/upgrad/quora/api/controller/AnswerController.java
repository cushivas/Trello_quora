package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/")
public class AnswerController {

    @Autowired
    AnswerBusinessService answerService;

    @Autowired
    QuestionBusinessService questionService;

    /**
     * Answer controller method for creating answer for a question
     * @param authorization
     * @param quesUuid
     * @param answerRequest
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */

    @RequestMapping(path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String quesUuid, final AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerRequest.getAnswer());
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerService.createAnswers(quesUuid, answerEntity, authorization);
        AnswerResponse answerRsp = new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerRsp, HttpStatus.CREATED);

    }

    /**
     * Edit answer controller, to let user edit answer when have required access
     * @param authorization
     * @param ansUuid
     * @param answerRequest
     * @return
     * @throws AuthorizationFailedException
     * @throws AnswerNotFoundException
     */

    @RequestMapping(path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswer(@RequestHeader("authorization") final String authorization, @PathVariable("answerId") final String ansUuid, final AnswerRequest answerRequest) throws AuthorizationFailedException, AnswerNotFoundException {

        answerService.editAnswer(ansUuid, answerRequest.getAnswer(), authorization);
        AnswerEditResponse answerRsp = new AnswerEditResponse().id(ansUuid).status("ANSWER EDITED");
        return new ResponseEntity<>(answerRsp, HttpStatus.OK);

    }

    /**
     * Delete Answer of Question if user has access
     *
     * @param answerUUid
     * @param authorizationToken
     * @return
     * @throws AnswerNotFoundException
     * @throws AuthorizationFailedException
     */

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteUser(@PathVariable("answerId") final String answerUUid, @RequestHeader("authorization") final String authorizationToken)
            throws AuthorizationFailedException, AnswerNotFoundException {
        final AnswerEntity answerEntity = answerService.deleteAnswer(answerUUid, authorizationToken);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, headers, HttpStatus.OK);
    }

    /**
     *  Method to retrieve all the answers for a question by Id
     * @param questionId
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersForQuestion(@PathVariable(value = "questionId") final String questionId,
                                                                                @RequestHeader(value = "authorization") final String authorization)
            throws AuthorizationFailedException, InvalidQuestionException {

        List<AnswerEntity> answerEntityList = answerService.getAllAnswersForQuestion(questionId, authorization);

        List<AnswerDetailsResponse> answerDetailsResponseList = new LinkedList<>();//list is created to return.

        for (AnswerEntity answerEntity : answerEntityList) {
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse().id(answerEntity.getUuid()).answerContent(answerEntity.getAns());
            answerDetailsResponseList.add(answerDetailsResponse);
        }
        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponseList, HttpStatus.OK);
    }
}
