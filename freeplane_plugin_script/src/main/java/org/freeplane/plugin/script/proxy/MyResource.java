package org.freeplane.plugin.script.proxy;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.freeplane.plugin.script.proxy.ServerCallback;
import org.freeplane.plugin.script.proxy.TaskExternal;
import org.freeplane.core.util.LogUtils;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

	static ServerCallback _interface;
	
	static public void registerInterface(ServerCallback _interface) {
		MyResource._interface = _interface;
	}

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Proxy.TaskExternal> getTasks() {
        if (_interface != null) {
        	LogUtils.info("calling callback");
        	ArrayList<Proxy.TaskExternal> res = _interface.getTasks();
        	LogUtils.info("got answer from callback: " + res.toString());
        	return res;
        }
        return null;
    }
}
