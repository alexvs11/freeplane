package org.freeplane.plugin.script.proxy;

import org.freeplane.plugin.script.proxy.Server;
import org.freeplane.plugin.script.proxy.ServerCallback;
import org.freeplane.plugin.script.proxy.TaskExternal;
import java.util.ArrayList;

abstract public class ServerInterfaceProxy implements Proxy.Server {
	static public void registerCallback(ServerCallback callback) {
		Server.getServer().registerInterface(callback);
	}
	
	abstract public ArrayList<Proxy.TaskExternal> getTasks();
}
