package org.freeplane.plugin.script.proxy;

import java.net.URI;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.freeplane.core.util.LogUtils;
import org.glassfish.grizzly.http.server.HttpServer;
import org.freeplane.plugin.script.proxy.ServerCallback;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class Server {
	private static Server _server;
	private static HttpServer _httpServer;
	private static final String BASE_URI = "http://0.0.0.0:8080/";
	private Server() {
		System.err.println("starting servlet conainer here");
		LogUtils.info("starting servlet conainer here");
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        _httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new ServerConfig());
	}
	static public Server getServer() {
		if (_server == null) {
			_server = new Server();
		}
		return _server;
	}
	static public void registerInterface(ServerCallback callback) {
		org.freeplane.plugin.script.proxy.MyResource.registerInterface(callback);
	}
}
