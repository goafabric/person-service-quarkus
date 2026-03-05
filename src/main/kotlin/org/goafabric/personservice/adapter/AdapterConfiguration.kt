package org.goafabric.personservice.adapter

import jakarta.ws.rs.core.MultivaluedMap
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory
import org.goafabric.personservice.extensions.UserContext
import org.jboss.resteasy.specimpl.MultivaluedMapImpl

class AdapterConfiguration : ClientHeadersFactory {

    override fun update(
        multivaluedMap: MultivaluedMap<String, String>,
        multivaluedMap1: MultivaluedMap<String, String>
    ): MultivaluedMap<String, String> {
        val result: MultivaluedMap<String, String> = MultivaluedMapImpl<String, String>()
        UserContext.adapterHeaderMap.forEach { (key: String, value: String) -> result.add(key, value) }
        return result
    }
}
