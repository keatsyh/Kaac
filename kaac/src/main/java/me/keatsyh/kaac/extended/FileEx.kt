package me.keatsyh.kaac.extended

import okhttp3.ResponseBody
import timber.log.Timber
import java.io.*

fun ResponseBody.toFile(path: String, fileName: String): Boolean {

    Timber.d("toFile  path:  $path")
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null
    try {
        val dir = File(path)
        if(!dir.exists()) {

            val mkdirs = dir.mkdirs()
            Timber.d("toFile  mkdirs $mkdirs")
        }

        val file = File("${dir.path}${File.separator}$fileName")
        inputStream = this.byteStream()
        outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024 * 4)
        var fileSizeDownloaded: Long = 0
        while (true) {
            val read = inputStream.read(buffer)
            if (read == -1) {
                break
            }
            outputStream.write(buffer, 0, read)
            fileSizeDownloaded += read
            Timber.d("toFile  fileSize:  $fileSizeDownloaded")
        }
        outputStream.flush()
        return true
    }catch (e: IOException){
        Timber.d("toFile  $e")
        e.printStackTrace()
        return false
    }finally {
        inputStream?.close()
        outputStream?.close()
    }


}