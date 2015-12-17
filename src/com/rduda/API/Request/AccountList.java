package com.rduda.API.Request;

import com.rduda.API.Account;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Transfer object containing a list of RequestMapping.
 */
public class AccountList {
    @QueryParam("result")
    private List<RequestMapping> list = new ArrayList<>();

    public AccountList() {
    }

    public AccountList(List<Account> accounts) {
        for (Account account : accounts) {
            list.add(new RequestMapping(account));
        }
    }

    public List<RequestMapping> getList() {
        return list;
    }

    public void setList(List<RequestMapping> list) {
        this.list = list;
    }
}
