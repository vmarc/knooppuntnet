package kpn.core.util

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

import scala.collection.mutable.ListBuffer
import scalax.file.Path

case class ZipFileEntry(name: String, contents: String)

object ZipFile {

  def read(filename: String): Seq[ZipFileEntry] = {
    val in = new ZipInputStream(new FileInputStream(filename))
    try {
      var entry = in.getNextEntry
      var entries = ListBuffer[ZipFileEntry]()
      while(entry != null) {
        val out = new ByteArrayOutputStream()
        val buffer = new Array[Byte](2048)
        var size = in.read(buffer, 0, buffer.length)
        while (size != -1) {
          out.write(buffer, 0, size)
          size = in.read(buffer, 0, buffer.length)
        }
        val contents = out.toString()
        entries += ZipFileEntry(entry.getName, contents)
        entry = in.getNextEntry
      }
      entries
    }
    finally {
      in.close()
    }
  }
}

class ZipFile(filename: String) {

  private val zip = {
    val f = new File(filename)
    val outputStream = new FileOutputStream(f)
    new ZipOutputStream(outputStream)
  }

  def add(entryName: String, content: String): Unit = {
    zip.putNextEntry(new ZipEntry(entryName))
    zip.write(content.getBytes("UTF-8"))
    zip.closeEntry()
  }

  def close(): Unit = {
    zip.close()
  }
}
