package org.goafabric.personservice.extensions

import io.quarkus.runtime.annotations.StaticInitSafe
import org.eclipse.microprofile.config.spi.ConfigSource
import java.io.File
import java.nio.file.Files

@StaticInitSafe
class ConfigTreeSource(private val root: File) : ConfigSource {

    private val properties: Map<String, String> = load()

    private fun load(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        root.walkTopDown()
            .filter { it.isFile }
            .forEach { file ->
                val relative = root.toPath().relativize(file.toPath()).toString()
                val key = relative.replace(File.separatorChar, '.').replace(Regex("\\.+"), ".")
                val value = Files.readString(file.toPath()).trim()
                map[key] = value
            }

        return map
    }

    override fun getProperties(): MutableMap<String, String> = properties.toMutableMap()

    override fun getPropertyNames(): MutableSet<String> = properties.keys.toMutableSet()

    override fun getValue(propertyName: String?): String? = properties[propertyName]

    override fun getName(): String = "configtree:${root.absolutePath}"

    override fun getOrdinal(): Int = 350
}