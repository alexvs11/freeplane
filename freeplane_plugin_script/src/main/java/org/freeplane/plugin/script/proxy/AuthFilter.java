package org.freeplane.plugin.script.proxy;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
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

public class AuthFilter extends BaseFilter {

    public AuthFilter() {
        super();
        LogUtils.info("AuthFilter constructed");
    }

    private final Credentials cred = getCredentials();

    private class Credentials {
        public String user;
        public String password;
    }

    private Credentials getCredentials() {
        Credentials cred = new Credentials();
        Properties gtdProps = new Properties();

        try {
            FileInputStream in = new FileInputStream("gtd.properties");
            gtdProps.load(in);
            in.close();

            cred.user = gtdProps.getProperty("user");
            cred.password = gtdProps.getProperty("password");
        } catch (Exception e) {
            LogUtils.warn("can't read credentials " + e.toString());
        }

        return cred;
    }

    @Override
    public NextAction handleRead(FilterChainContext ctx) throws IOException {
        LogUtils.info("LogFilter handleRead");
                   //new Object[]{ctx.getConnection(), ctx.getMessage()});

        final HttpContent httpContent = ctx.getMessage();
        if(httpContent.getHttpHeader().containsHeader("Authorization")) {

            final String authHeaderReq = httpContent.getHttpHeader().getHeader("Authorization");
            String authHeader = cred.user + ":" + cred.password;

            final String authHeaderExpected = "Basic " + Base64Utils.encodeToString(authHeader.getBytes(), false);

            LogUtils.info("authHeaderReq: " + authHeaderReq);
            LogUtils.info("authHeaderExpected: " + authHeaderExpected);

            if(authHeaderExpected.equals(authHeaderReq)) {
                return ctx.getInvokeAction();
            } else {
                final HttpResponsePacket response = ((HttpRequestPacket) httpContent.getHttpHeader()).getResponse();
                response.setStatus(403);
                ctx.getConnection().write(response);

                return ctx.getStopAction();

            }

        } else {
            final HttpResponsePacket response = ((HttpRequestPacket) httpContent.getHttpHeader()).getResponse();
            response.setStatus(401);
            response.setHeader(Header.WWWAuthenticate, "Basic");
            ctx.getConnection().write(response);

            return ctx.getStopAction();
        }
    }

    @Override
    public NextAction handleWrite(FilterChainContext ctx) throws IOException {
        LogUtils.info("LogFilter handleWrite. Connection={0} message={1}");
                 //  new Object[]{ctx.getConnection(), ctx.getMessage()});
        return ctx.getInvokeAction();
    }
}