package com.rduda.Model;

import com.rduda.API.Account;
import com.rduda.API.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2015-11-23.
 *
 * * Creates an instanced copy of any object implementing Message.
 */
class MessageBean implements Message {
    private Long id;
    private String content;
    private String date;
    private Account receiver;
    private Account sender;

    public MessageBean(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.date = message.getDate();
        this.receiver = new AccountBean(message.getReceiver());
        this.sender = new AccountBean(message.getSender());
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public String getDate() {
        return this.date;
    }

    @Override
    public Account getReceiver() {
        return this.receiver;
    }

    @Override
    public Account getSender() {
        return this.sender;
    }

    public static List<Message> FromList(List<Message> messages) {
        List<Message> list = new ArrayList<>();

        for (Message message : messages) {
            list.add(new MessageBean(message));
        }
        return list;
    }
}
