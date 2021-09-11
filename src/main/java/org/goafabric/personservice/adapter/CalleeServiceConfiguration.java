package org.goafabric.personservice.adapter;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Base64;

public class CalleeServiceConfiguration implements ClientHeadersFactory {
    @ConfigProperty(name = "adapter.calleeservice.user")
    String user;

    @ConfigProperty(name = "adapter.calleeservice.password")
    String password;

    @ConfigProperty(name = "quarkus.http.auth.basic")
    Boolean basicAuth;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> multivaluedMap, MultivaluedMap<String, String> multivaluedMap1) {
        if (!basicAuth) {
            return multivaluedMap;
        }
        final MultivaluedMap<String, String> result = new MultivaluedMapImpl<>();
        result.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(
                (new String(Base64.getDecoder().decode(user)) + ":" + new String(Base64.getDecoder().decode(password))).getBytes()));
        return result;
    }
}
