package com.rduda.Model;

import com.rduda.API.Image;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by Robin on 2015-11-25.
 * <p>
 * Hibernate mapping for an image in the database.
 */

@Entity
@Table(name = "image")
class ImageMapping implements Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base64")
    @Type(type = "text")
    private String base64;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return this.base64;
    }
}
