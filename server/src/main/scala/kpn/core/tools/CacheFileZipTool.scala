package kpn.core.tools

import java.io.File

import kpn.core.util.GZipFile

import scala.io.Source

object CacheFileZipTool {
  def main(args: Array[String]): Unit = {
    new CacheFileZipTool().processCacheFiles()
  }
}

class CacheFileZipTool {

  private val sourceDir = "/kpn/cache"
  private val targetDir = "/kpn/cache-gz"

  def processCacheFiles(): Unit = {
    processCacheFileDir(new File(sourceDir))
  }

  private def processCacheFileDir(dir: File): Unit = {
    val files = dir.listFiles.toSeq
    files.foreach { file =>
      if (file.isDirectory) {
        processCacheFileDir(file)
      }
      else {
        process(file)
      }
    }
  }

  private def process(file: File): Unit = {

    val sourceFileDir = file.getParent
    val targetFileDir = targetDir + sourceFileDir.substring(sourceDir.length)
    val sourceFilename = file.getAbsolutePath
    val targetFilename = targetDir + sourceFilename.substring(sourceDir.length) + ".gz"

    new File(targetFileDir).mkdirs()

    val xml = Source.fromFile(sourceFilename).mkString
    GZipFile.write(targetFilename, xml)

    println(targetFilename)
  }
}
