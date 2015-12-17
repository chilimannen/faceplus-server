package Mock;

import com.rduda.API.Account;
import com.rduda.API.Post;
import com.rduda.API.PostStore;
import com.rduda.Model.Exception.NoSuchFriendException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2015-11-18.
 */
class PostStoreMock implements PostStore {
    private static List<Post> posts = new ArrayList<>();
    private static Long postId = 0L;

    @Override
    public void addPost(Post post) {
        ((PostMock) post).setId(postId++);
        posts.add(post);
    }

    @Override
    public void removePost(Account actor, long postId) {

        for (Post post : new ArrayList<>(posts)) {
            if (post.getId() == postId
                    && (post.getReceiver().equals(actor) || post.getSender().equals(actor))) {
                posts.remove(post);
            }
        }
    }

    @Override
    public List<Post> getHomePosts(Account account) {
        List<Post> homePosts = new ArrayList<>();

        for (Post post : posts) {
            if (post.getReceiver().equals(account))
                homePosts.add(post);
        }
        return homePosts;
    }

    @Override
    public List<Post> getStreamPosts(Account actor) {
        List<Post> streamPosts = new ArrayList<>();
        AccountMock account = (AccountMock) actor;

        for (Post post : posts) {
            if (post.getSender().equals(post.getReceiver()))
                if (account.getFriendList().contains(post.getSender()))
                    streamPosts.add(post);
        }
        return streamPosts;
    }

    @Override
    public List<Post> getFriendPosts(Account actor, Account friend) throws NoSuchFriendException {
        List<Post> friendPosts = new ArrayList<>();
        AccountMock account = (AccountMock) actor;

        if (account.getFriendList().contains(friend)) {
            for (Post post : posts) {
                if (post.getReceiver().equals(friend))
                    friendPosts.add(post);
            }
        } else
            throw new NoSuchFriendException();

        return friendPosts;
    }
}
