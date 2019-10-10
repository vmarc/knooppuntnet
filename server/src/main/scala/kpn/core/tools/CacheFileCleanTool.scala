package kpn.core.tools

import java.io.File

case class InitialLoadDir(relativeDirName: String, fileCount: Int)

object CacheFileCleanTool {
  def main(args: Array[String]): Unit = {
    new CacheFileCleanTool("/kpn/cache/").removeInitialLoadFiles()
  }
}

class CacheFileCleanTool(sourceDir: String) {

  def removeInitialLoadFiles(): Unit = {

    val dirs = initialLoadDirs(new File(sourceDir), 0)
    dirs.foreach { dir =>
      println(s"rm -rf ${dir.relativeDirName}")
    }

    println(s"# initial load dirs found: ${dirs.size}")
    println(s"# files cleaned: ${dirs.map(_.fileCount).sum}")
  }

  private def initialLoadDirs(dir: File, level: Int): Seq[InitialLoadDir] = {
    if (level == 6) {
      processLeafDir(dir)
    }
    else {
      val files = dir.listFiles.toSeq.sorted
      files.flatMap { file =>
        if (file.isDirectory) {
          initialLoadDirs(file, level + 1)
        }
        else {
          None
        }
      }
    }
  }

  private def processLeafDir(dir: File): Seq[InitialLoadDir] = {
    val fileCount = dir.listFiles.length
    if (fileCount > 1000) {
      val relativeDirName = dir.getAbsolutePath.substring(sourceDir.length)
      println(s"$relativeDirName, size=" + fileCount)
      Seq(InitialLoadDir(relativeDirName, fileCount))
    }
    else {
      Seq()
    }
  }
}
