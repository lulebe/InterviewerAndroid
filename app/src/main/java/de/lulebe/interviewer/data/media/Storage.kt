package de.lulebe.interviewer.data.media

import android.content.Context
import java.io.File



fun getUsedStorage(context: Context) : Int {
    return dirSize(context.filesDir)
}

fun getTotalStorage() = 100000000

private fun dirSize(dir: File): Int {
    if (dir.exists()) {
        var result: Int = 0
        val fileList = dir.listFiles()
        for (i in fileList.indices) {
            // Recursive call if it's a directory
            if (fileList[i].isDirectory) {
                result += dirSize(fileList[i])
            } else {
                // Sum the file size in bytes
                result += fileList[i].length().toInt()
            }
        }
        return result // return the file size
    }
    return 0
}