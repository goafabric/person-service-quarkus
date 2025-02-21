package org.goafabric.personservice.adapter;

import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.goafabric.personservice.extensions.TenantContext;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

public class AdapterConfiguration implements ClientHeadersFactory {
    @ConfigProperty(name = "adapter.calleeservice.user.name")
    String user;

    @ConfigProperty(name = "adapter.calleeservice.user.password")
    String password;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> multivaluedMap, MultivaluedMap<String, String> multivaluedMap1) {
        final MultivaluedMap<String, String> result = new MultivaluedMapImpl<>();
        TenantContext.getAdapterHeaderMap().forEach(result::add);
        return result;
    }
}
