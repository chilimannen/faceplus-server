package com.rduda.API.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rduda.API.Message;

import javax.ws.rs.QueryParam;

/**
 * Created by Robin on 2015-11-29.
 * <p>
 * Message transfer object.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageMapping implements Message {
    @QueryParam("sender")
    private RequestMapping sender;

    @QueryParam("receiver")
    private RequestMapping receiver;

    @QueryParam("content")
    private String content;

    @QueryParam("date")
    private String date;

    @QueryParam("id")
    private Long id;

    @QueryParam("token")
    private String token;

    @QueryParam("actor")
    private String actor;

    @QueryParam("target")
    private String target;

    public MessageMapping() {
    }

    public MessageMapping(RequestMapping account) {
        this.token = account.getToken();
        this.actor = account.getActor();
    }

    public MessageMapping(Message message) {
        this.content = message.getContent();
        this.sender = new RequestMapping(message.getSender());
        this.receiver = new RequestMapping(message.getReceiver());
        this.id = message.getId();
        this.date = message.getDate();
    }

    public RequestMapping getSender() {
        return sender;
    }

    public void setSender(RequestMapping sender) {
        this.sender = sender;
    }

    public RequestMapping getReceiver() {
        return receiver;
    }

    public MessageMapping setReceiver(RequestMapping account) {
        this.receiver = account;
        return this;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public MessageMapping setContent(String content) {
        this.content = content;
        return this;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public MessageMapping setToken(String token) {
        this.token = token;
        return this;
    }

    public String getActor() {
        return actor;
    }

    public MessageMapping setActor(String actor) {
        this.actor = actor;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public MessageMapping setTarget(String target) {
        this.target = target;
        return this;
    }
}

