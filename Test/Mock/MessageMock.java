package Mock;

import com.rduda.API.Account;
import com.rduda.API.Message;

import java.util.Date;

/**
 * Created by Robin on 2015-11-20.
 *
 * Mock for the Message Model.
 */
class MessageMock implements Message {
    private static Integer next_id = 0;
    private long id;
    private String content;
    private String date;
    private Account sender;
    private Account receiver;

    public MessageMock(Account sender, Account receiver, String content) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.date = new Date().toString();
        this.id = getNextId();
    }

    @Override
    public Long getId() {
        return id;
    }

    private long getNextId() {
        synchronized (MessageMock.class) {
            this.id = next_id;
            next_id += 1;
            return next_id;
        }
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
}
