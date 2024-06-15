package com.example.newsletters.utils

object FileUtils {
    fun String.extensionFile(): String = let { name -> if (name.contains(".")) name.substring(lastIndexOf(".") + 1) else "" }

    fun getFileByNameWithoutExtension(filename: String): String =
        filename.let { name -> if (name.contains(".")) name.substring(0, filename.lastIndexOf(".")) else "" }
}
