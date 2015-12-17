package com.rduda.API.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rduda.API.Account;

import javax.ws.rs.QueryParam;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Account/Query transfer object.
 * <p>
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestMapping implements Account {
    @QueryParam("actor")
    private String actor;

    @QueryParam("password")
    private String password;

    @QueryParam("firstName")
    private String firstName;

    @QueryParam("lastName")
    private String lastName;

    @QueryParam("country")
    private String country;

    @QueryParam("age")
    private Integer age;

    @QueryParam("id")
    private Long id;

    @QueryParam("profileImage")
    private Long profileImage;

    @QueryParam("profileImageData")
    private String profileImageData;

    @QueryParam("token")
    private String token;

    @QueryParam("authenticated")
    private Boolean authenticated;

    @QueryParam("result")
    private Boolean result;

    @QueryParam("search")
    private String search;

    @QueryParam("target")
    private String target;

    public RequestMapping() {
    }

    public RequestMapping(RequestMapping account) {
        this.token = account.getToken();
        this.actor = account.getActor();
    }

    public RequestMapping(Account account) {
        this.actor = account.getActor();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.country = account.getCountry();
        this.age = account.getAge();
        this.id = account.getId();
        this.token = account.getToken();
        this.profileImage = account.getProfileImage();
        this.authenticated = true;
    }

    public RequestMapping setActor(String actor) {
        this.actor = actor;
        return this;
    }

    public RequestMapping setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public RequestMapping setPassword(String password) {
        this.password = password;
        return this;
    }

    public RequestMapping setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public RequestMapping setToken(String token) {
        this.token = token;
        return this;
    }

    public RequestMapping setCountry(String country) {
        this.country = country;
        return this;
    }

    public RequestMapping setAge(Integer age) {
        this.age = age;
        return this;
    }

    public RequestMapping setProfileImageData(String profileImageBase64) {
        this.profileImageData = profileImageBase64;
        return this;
    }

    public RequestMapping setProfileImage(Long profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public String getActor() {
        return actor;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    public RequestMapping setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getProfileImage() {
        return profileImage;
    }

    public String getProfileImageData() {
        return profileImageData;
    }

    public String getToken() {
        return token;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getSearch() {
        return search;
    }

    public RequestMapping setSearch(String search) {
        this.search = search;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public RequestMapping setTarget(String target) {
        this.target = target;
        return this;
    }

    public Boolean getResult() {
        return result;
    }

    public RequestMapping setResult(Boolean result) {
        this.result = result;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        boolean equals = false;

        if (other instanceof RequestMapping) {
            RequestMapping json = (RequestMapping) other;

            if (json.id.equals(id))
                equals = true;
        }

        return equals;
    }


}
