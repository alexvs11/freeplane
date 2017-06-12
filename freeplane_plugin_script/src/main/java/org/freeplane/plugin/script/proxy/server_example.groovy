package org.freeplane.plugin.script.proxy

import org.freeplane.plugin.script.proxy.*

/**
 * Created by alex on 12.06.17.
 * just demonstation of usage
 */

class MyCallback implements ServerCallback {
    ArrayList<TaskExternal> getTasks() {
        println "hello world"
        return []
    }
}

ServerInterfaceProxy.registerCallback(new MyCallback())