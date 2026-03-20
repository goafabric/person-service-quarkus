package org.goafabric.personservice.extensions

import io.smallrye.config.ConfigSourceContext
import io.smallrye.config.ConfigSourceFactory
import org.eclipse.microprofile.config.spi.ConfigSource
import java.io.File
import java.util.*

//https://quarkus.io/guides/config-extending-support#custom-config-source
class ConfigTreeSourceFactory : ConfigSourceFactory {

    override fun getConfigSources(context: ConfigSourceContext): Iterable<ConfigSource> {
        return getConfig(File(context.getValue("quarkus.configtree.path") ?.value ?: ""))
    }

    private fun getConfig(directory: File): Iterable<ConfigSource> {
        return if (!directory.name.equals("") && directory.exists() && directory.isDirectory) listOf(ConfigTreeSource(directory)) else emptyList()
    }

    override fun getPriority(): OptionalInt = OptionalInt.of(290)
}