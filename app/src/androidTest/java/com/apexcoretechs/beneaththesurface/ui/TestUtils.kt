package com.apexcoretechs.beneaththesurface.ui

import android.content.Context

fun loadJsonFromAssets(context: Context, filename: String): String {
    return object {}.javaClass.classLoader!!
        .getResource(filename)!!
        .readText()}
