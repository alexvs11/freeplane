package org.freeplane.plugin.script.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import org.freeplane.core.util.LogUtils;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.glassfish.grizzly.http.HttpContent;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.http.HttpResponsePacket;
import org.glassfish.grizzly.http.util.Base64Utils;
import org.glassfish.grizzly.http.util.Header;
import org.freeplane.core.util.LogUtils;
import org.glassfish.jersey.internal.util.Base64;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

// inspired by http://howtodoinjava.com/jersey/jersey-rest-security/

@Provider
public class AuthFilter implements javax.ws.rs.container.ContainerRequestFilter {

    public AuthFilter() {
        super();
        LogUtils.info("AuthFilter constructed");
    }

    private final Credentials cred = getCredentials();

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("You cannot access this resource").build();
    private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
            .entity("Access blocked for all users !!").build();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        //Access allowed for all
        if( ! method.isAnnotationPresent(PermitAll.class))
        {
            //Access denied for all
            if(method.isAnnotationPresent(DenyAll.class))
            {
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }

            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();

            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            //If no authorization information present; block access
            if(authorization == null || authorization.isEmpty())
            {
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }

            //Get encoded username and password
            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

            //Decode username and password
            String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;
            LogUtils.info("got credentials1: " + usernameAndPassword);

            if (!usernameAndPassword.equals(cred.user + ":" + cred.password)) {
                requestContext.abortWith(ACCESS_DENIED);
            }

            return;
        }
    }

    private class Credentials {
        public String user;
        public String password;
    }

    private Credentials getCredentials() {
        Credentials cred = new Credentials();
        Properties gtdProps = new Properties();

        try {
            String path = AuthFilter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            LogUtils.warn("loading from " + path);

            gtdProps.load(ClassLoader.getSystemResourceAsStream("resources/gtd.properties"));
//            FileInputStream in = new FileInputStream("resources/gtd.properties");
//            gtdProps.load(in);
//            in.close();

            cred.user = gtdProps.getProperty("user");
            cred.password = gtdProps.getProperty("password");
        } catch (Exception e) {
            LogUtils.warn("can't read credentials " + e.toString());
        }

        return cred;
    }
}