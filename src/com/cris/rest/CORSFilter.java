package com.cris.rest;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;

public class CORSFilter implements ContainerResponseFilter {

    /*public ContainerResponse filter(ContainerRequest creq, ContainerResponse cresp) {

        

        return cresp;
    }*/

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		requestContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
		requestContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
		requestContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
		requestContext.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");		
	}
}
