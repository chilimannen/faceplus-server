package com.rduda.Controller;

import com.rduda.API.Version;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Robin on 2015-11-29.
 * <p>
 * Serves service descriptor.
 */

@Path("/meta")
public class ProtocolRoute {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response info() {
        return Response.ok(new Version()).build();
    }

}
