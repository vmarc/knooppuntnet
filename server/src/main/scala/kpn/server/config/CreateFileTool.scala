package kpn.server.config

import java.io.FileWriter
import java.io.PrintWriter
import java.io.StringWriter

object CreateFileTool {
  def main(args: Array[String]): Unit = {
    val fileWriter = new FileWriter("/home/vmarc/21mb.txt")
    val out = new PrintWriter(fileWriter)
    val word = "1234567890"
    val kilobyte = (1 to 105).map(w => word).mkString.substring(0, 1023)
    1 to 21 foreach { i =>
      1 to 1024 foreach { j =>
        out.println(kilobyte)
      }
    }
    out.close()
  }
}
