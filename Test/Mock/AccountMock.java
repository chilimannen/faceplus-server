package Mock;

import com.rduda.API.Account;
import com.rduda.API.Image;
import com.rduda.API.Message;
import com.rduda.API.Post;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2015-11-13.
 * <p>
 * Mock for the Account class.
 */
class AccountMock implements Account {
    private Long id;
    private Integer age;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String country;
    private List<Account> requests = new ArrayList<>();
    private List<Account> friends = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private Image profileImage;


    public AccountMock(String username, String plaintextPassword, String firstName, String lastName) {
        this.username = username;
        this.password = plaintextPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = (long) username.hashCode();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getActor() {
        return username;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public List<Account> getFriendRequest() {
        return requests;
    }

    public List<Account> getFriendList() {
        return friends;
    }

    public List<Post> getHomePosts() {
        return posts;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Account) {
            Account account = (Account) other;

            if (account.getId().equals(id))
            return true;
        }
        return false;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public Long getProfileImage() {
        throw new NotImplementedException();
    }

    public Image getProfileImageMock() {
        return profileImage;
    }
}
