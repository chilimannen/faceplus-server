package com.rduda.Model;

import com.rduda.API.Account;
import com.rduda.API.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2015-11-23.
 *
 * Creates an instanced copy of any object implementing Post.
 */
class PostBean implements Post {
    private long id;
    private String content;
    private String date;
    private Account receiver;
    private Account sender;

    public PostBean(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.date = post.getDate();
        this.receiver = new AccountBean(post.getReceiver());
        this.sender = new AccountBean(post.getSender());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public Account getReceiver() {
        return receiver;
    }

    @Override
    public Account getSender() {
        return sender;
    }

    public static List<Post> FromList(List<Post> posts) {
        List<Post> list = new ArrayList<>();

        for (Post post : posts) {
            list.add(new PostBean(post));
        }
        return list;
    }
}
