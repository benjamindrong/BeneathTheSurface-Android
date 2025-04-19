package com.apexcoretechs.beneaththesurface.viewmodel

fun loadTestJson(fileName: String): String {
    return object {}.javaClass.classLoader!!
        .getResource(fileName)!!
        .readText()
}