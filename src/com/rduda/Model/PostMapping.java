package com.rduda.Model;

import com.rduda.API.Post;

import javax.persistence.*;

/**
 * Created by Robin on 2015-11-18.
 * <p>
 * Post model as Hibernate entity.
 */

@Entity
@Table(name = "post")
class PostMapping implements Post {
    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private String date;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "sender")
    private AccountMapping sender;


    @ManyToOne
    @JoinColumn(name = "receiver")
    private AccountMapping receiver;

    public AccountMapping getSender() {
        return sender;
    }

    public void setSender(AccountMapping sender) {
        this.sender = sender;
    }

    public AccountMapping getReceiver() {
        return receiver;
    }

    public void setReceiver(AccountMapping receiver) {
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
