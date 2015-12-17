package com.rduda.Controller;

import com.rduda.API.Request.AccountList;
import com.rduda.API.Request.PostMapping;
import com.rduda.API.Request.PostList;
import com.rduda.API.Request.RequestMapping;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Created by Robin on 2015-11-28.
 * <p>
 * Configures the JERSEY-REST with classes for JACKSON-JSON.
 */
@ApplicationPath("api")
public class ApplicationConfig extends Application {

    public ApplicationConfig() {
        new ResourceConfig(
                com.rduda.API.Account.class,
                RequestMapping.class,
                AccountList.class,
                ProtocolRoute.class,
                AccountRoute.class,
                FriendRoute.class,
                PostRoute.class,
                PostMapping.class,
                PostList.class
        ).register(JacksonFeature.class);
    }

}
