package org.freeplane.plugin.script.proxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.HashMap;
import java.util.Map;

public class ServerConfig extends ResourceConfig {
    public ServerConfig () {
        super();
        // create custom ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        //TBD: register as class
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);
        ServerExceptionMapper mapperException = new ServerExceptionMapper();

        this.register(org.freeplane.plugin.script.proxy.MyResource.class);
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package
        this.register(provider);
        this.register(mapperException);
        this.register(AuthFilter.class);
    }

}
