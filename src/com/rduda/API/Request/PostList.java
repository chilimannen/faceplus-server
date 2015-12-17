package com.rduda.API.Request;

import com.rduda.API.Post;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2015-11-29.
 * <p>
 * Transfer object containing a list of PostMapping.
 */
public class PostList {
    @QueryParam("result")
    private List<PostMapping> list = new ArrayList<>();

    public PostList() {
    }

    public static PostList fromList(List<Post> posts) {
        PostList result = new PostList();

        for (Post post : posts) {
            result.list.add(new PostMapping(post));
        }
        return result;
    }

    public List<PostMapping> getList() {
        return list;
    }

    public void setList(List<PostMapping> list) {
        this.list = list;
    }
}
