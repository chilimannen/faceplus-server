package com.rduda.API;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Robin on 2015-11-29.
 * <p>
 * Contains API version.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Version {
    private String version = "1.01";
    private String author = "Robin Duda";
    private String servedBy;

    public Version() {
        try {
            servedBy = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getServedBy() {
        return servedBy;
    }

    public void setServedBy(String servedBy) {
        this.servedBy = servedBy;
    }
}
