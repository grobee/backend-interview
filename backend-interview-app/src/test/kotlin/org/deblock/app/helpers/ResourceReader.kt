package org.deblock.org.deblock.app.helpers

import java.nio.file.Files
import org.springframework.util.ResourceUtils

object ResourceReader {

    fun readResourceAsString(path: String): String =
        Files.readString(ResourceUtils.getFile("classpath:$path").toPath())
}