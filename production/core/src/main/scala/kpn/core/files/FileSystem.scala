package kpn.core.files

import java.io.File

trait FileSystem {

  def baseDir: String

  def deleteFile(name: String): Unit

  def deleteDirectory(name: String): Unit

  def putFile(local: File, dest: String): Unit

  def getFile(local: File, dest: String): Unit

  def createDirectory(name: String): Unit

  def listFiles(dir: String): Seq[FsFile]

  def close(): Unit

  def fullPath(relativePath: String): String = {
    FsUtils.withTrailingSlash(baseDir) + FsUtils.withoutLeadingSlash(relativePath)
  }

}
