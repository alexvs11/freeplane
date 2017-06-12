package org.freeplane.plugin.script.proxy;
import java.util.ArrayList;

import org.freeplane.plugin.script.proxy.TaskExternal;


public interface ServerCallback {
	ArrayList<Proxy.TaskExternal> getTasks();
}
