package com.rduda.API.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rduda.API.Post;

import javax.ws.rs.QueryParam;

/**
 * Created by Robin on 2015-11-29.
 * <p>
 * Post transfer object.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostMapping implements Post {
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

    @QueryParam("actor")
    private String actor;

    @QueryParam("token")
    private String token;

    @QueryParam("target")
    private String target;

    public PostMapping() {
    }

    public PostMapping(RequestMapping mapping) {
        this.actor = mapping.getActor();
        this.token = mapping.getToken();
    }

    public PostMapping(Post post) {
        this.content = post.getContent();
        this.sender = new RequestMapping(post.getSender());
        this.receiver = new RequestMapping(post.getReceiver());
        this.id = post.getId();
        this.date = post.getDate();
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

    public void setReceiver(RequestMapping receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public PostMapping setContent(String content) {
        this.content = content;
        return this;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public PostMapping setId(Long id) {
        this.id = id;
        return this;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTarget() {
        return target;
    }

    public PostMapping setTarget(String target) {
        this.target = target;
        return this;
    }
}
