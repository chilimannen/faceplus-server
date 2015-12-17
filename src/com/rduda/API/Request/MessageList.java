package com.rduda.API.Request;


import com.rduda.API.Message;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2015-11-29.
 * <p>
 * Transfer object containing a list of MessageMapping.
 */
public class MessageList {
    @QueryParam("result")
    private List<MessageMapping> list = new ArrayList<>();

    public MessageList() {
    }

    public static MessageList fromList(List<Message> messages) {
        MessageList result = new MessageList();

        for (Message message : messages) {
            result.list.add(new MessageMapping(message));
        }
        return result;
    }

    public List<MessageMapping> getList() {
        return list;
    }

    public void setList(List<MessageMapping> list) {
        this.list = list;
    }
}
