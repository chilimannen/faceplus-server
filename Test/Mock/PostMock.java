package Mock;

import com.rduda.API.Account;
import com.rduda.API.Post;

import java.util.Date;

/**
 * Created by Robin on 2015-11-18.
 */
class PostMock implements Post {
    private long id;
    private String content;
    private String date;
    private Account receiver;
    private Account sender;


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

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
        this.date = new Date().toString();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
