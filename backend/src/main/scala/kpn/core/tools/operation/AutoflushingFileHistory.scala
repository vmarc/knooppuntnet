package kpn.core.tools.operation

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

import jline.console.history.FileHistory

class AutoflushingFileHistory(file: File) extends FileHistory(file) {

  private val out = new PrintWriter(new FileWriter(file, true), true)

  override def flush(): Unit = {
  }

  override def add(item: CharSequence): Unit = {
    super.add(item)
    out.println(item)
  }

}
