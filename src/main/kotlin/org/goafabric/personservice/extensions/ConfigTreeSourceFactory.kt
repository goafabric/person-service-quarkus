package org.goafabric.personservice.extensions

import io.smallrye.config.ConfigSourceContext
import io.smallrye.config.ConfigSourceFactory
import org.eclipse.microprofile.config.spi.ConfigSource
import java.io.File
import java.util.*

//https://quarkus.io/guides/config-extending-support#custom-config-source
class ConfigTreeSourceFactory : ConfigSourceFactory {

    override fun getConfigSources(context: ConfigSourceContext): Iterable<ConfigSource> {

        val path = context.getValue("quarkus.configtree.path")
            ?.value
            ?: return emptyList()

        val dir = File(path)

        if (!dir.exists() || !dir.isDirectory) {
            return emptyList()
        }

        return listOf(ConfigTreeSource(dir))
    }

    override fun getPriority(): OptionalInt = OptionalInt.of(290)
}