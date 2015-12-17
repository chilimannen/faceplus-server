package com.rduda.Model;

import com.rduda.API.Account;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Robin on 2015-11-11.
 * <p>
 * Hibernate entity class for Accounts.
 * Index on username as it is unique.
 */

@Entity
@Table(name = "account", indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "firstName"),
        @Index(columnList = "lastName")})
class AccountMapping implements Account {

    @Column(name = "password")
    private String password = "";

    @Column(name = "username", unique = true, nullable = false)
    private String username = "";

    @Column(name = "session")
    private String token = "";

    @Column(name = "salt")
    private String salt = "";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = -1L;


    @Column(name = "firstName", nullable = false)
    private String firstName = "";

    @Column(name = "lastName", nullable = false)
    private String lastName = "";

    @Column(name = "country")
    private String country = "";

    @Column(name = "age")
    private Integer age;


    @Column(name = "profileImage")
    private Long profileImage;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    @OrderBy("id DESC")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<MessageMapping> messages;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    @OrderBy("date DESC")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<PostMapping> sender;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<PostMapping> posts;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "request")
    private Collection<AccountMapping> requests;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("username ASC")
    @JoinTable(name = "friend")
    private Collection<AccountMapping> friends;


    public Collection<MessageMapping> getMessages() {
        return messages;
    }

    public void setMessages(Collection<MessageMapping> messages) {
        this.messages = messages;
    }

    public Collection<PostMapping> getSender() {
        return sender;
    }

    public void setSender(Collection<PostMapping> posts) {
        this.sender = posts;
    }

    public Collection<PostMapping> getPosts() {
        return posts;
    }

    public void setPosts(Collection<PostMapping> posts) {
        this.posts = posts;
    }

    public Collection<AccountMapping> getFriends() {
        return friends;
    }

    public void setFriends(Collection<AccountMapping> friends) {
        this.friends = friends;
    }

    public Collection<AccountMapping> getRequests() {
        return requests;
    }

    public void setRequests(Collection<AccountMapping> requests) {
        this.requests = requests;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getHashedPassword() {
        return password;
    }

    public void setHashedPassword(String password) {
        this.password = password;
    }

    public Account setPassword(String password) {
        this.salt = HashHelper.generateSalt();
        this.password = HashHelper.hash(password, salt);
        return this;
    }

    public String getActor() {
        return username;
    }

    public AccountMapping setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public AccountMapping setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public AccountMapping setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AccountMapping setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public AccountMapping setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Long getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Long profileImage) {
        this.profileImage = profileImage;
    }

    public AccountMapping setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        boolean equals = false;

        if (object instanceof Account) {
            Account other = (Account) object;

            if (other.getId().equals(id))
                equals = true;
        }
        return equals;
    }
}
