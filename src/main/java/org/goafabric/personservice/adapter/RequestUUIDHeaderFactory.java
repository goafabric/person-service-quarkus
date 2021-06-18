package org.goafabric.personservice.adapter;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Base64;

public class RequestUUIDHeaderFactory implements ClientHeadersFactory {

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> multivaluedMap, MultivaluedMap<String, String> multivaluedMap1) {
        MultivaluedMap<String, String> result = new MultivaluedMapImpl<>();
        result.add("Authorization", "Basic " +
                Base64.getEncoder().encodeToString("admin:admin".getBytes()));
        return result;
    }
}
