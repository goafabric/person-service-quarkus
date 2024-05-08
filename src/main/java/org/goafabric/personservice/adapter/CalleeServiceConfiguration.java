package org.goafabric.personservice.adapter;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.goafabric.personservice.extensions.HttpInterceptor;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import jakarta.ws.rs.core.MultivaluedMap;
import java.util.Base64;

public class CalleeServiceConfiguration implements ClientHeadersFactory {
    @ConfigProperty(name = "adapter.calleeservice.user.name")
    String user;

    @ConfigProperty(name = "adapter.calleeservice.user.password")
    String password;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> multivaluedMap, MultivaluedMap<String, String> multivaluedMap1) {
        final MultivaluedMap<String, String> result = new MultivaluedMapImpl<>();
        result.add("Authorization", "Basic " + Base64.getEncoder().encodeToString((user + ":" + password).getBytes()));
        result.add("X-TenantId", HttpInterceptor.getTenantId());
        result.add("X-Auth-Request-Preferred-Username", HttpInterceptor.getUserName());
        return result;
    }
}
