package kpn.core.util

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintStream
import java.io.Reader
import java.io.StringWriter
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object GZipFile {

  def read(filename: String): String = {
    var reader: Reader = null
    var writer: StringWriter = null
    try {
      val gzippedResponse: InputStream = new FileInputStream(filename)
      val ungzippedResponse: InputStream = new GZIPInputStream(gzippedResponse)
      reader = new InputStreamReader(ungzippedResponse, "UTF-8")
      writer = new StringWriter()
      val buffer: Array[Char] = Array[Char](10240)

      {
        var length2: Int = 0
        while ( {
          length2 = reader.read(buffer)
          length2
        } > 0) {
          writer.write(buffer, 0, length2)
        }
      }
    }
    finally {
      writer.close()
      reader.close()
    }
    writer.toString
  }

  def write(filename: String, string: String): Unit = {
    val fos = new FileOutputStream(filename)
    val os = new GZIPOutputStream(fos)
    val ps = new PrintStream(os)
    ps.print(string)
    ps.flush()
    ps.close()
  }
}
