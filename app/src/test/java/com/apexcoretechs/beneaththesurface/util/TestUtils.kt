package com.apexcoretechs.beneaththesurface.util

fun loadTestJson(fileName: String): String {
    return object {}.javaClass.classLoader!!
        .getResource(fileName)!!
        .readText()
}