package com.rduda.Model;

import com.rduda.API.Message;

import javax.persistence.*;

/**
 * Created by Robin on 2015-11-20.
 * <p>
 * Message model as Hibernate entity.
 */
@Entity
@Table(name = "message")
class MessageMapping implements Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private String date;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private AccountMapping receiver;

    @ManyToOne
    @JoinColumn(name = "sender")
    private AccountMapping sender;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AccountMapping getReceiver() {
        return receiver;
    }

    public void setReceiver(AccountMapping receiver) {
        this.receiver = receiver;
    }

    public AccountMapping getSender() {
        return sender;
    }

    public void setSender(AccountMapping sender) {
        this.sender = sender;
    }
}
