package kpn.core.files

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object LocalFileSystem {

  private class LocalFile(file: File) extends FsFile {

    override def isFile: Boolean = file.isFile

    override def isDirectory: Boolean = file.isDirectory

    override def name: String = file.getName

    override def toFile: File = file
  }

}

class LocalFileSystem(val baseDir: String) extends FileSystem {

  override def deleteFile(name: String): Unit = {
    file(name).delete
  }

  override def deleteDirectory(name: String): Unit = {
    file(name).delete
  }

  override def putFile(local: File, dest: String): Unit = {
    FsUtils.copy(new FileInputStream(local), new FileOutputStream(fullPath(dest)))
  }

  override def retrieveFile(local: File, dest: String): Unit = {
    FsUtils.copy(new FileInputStream(fullPath(dest)), new FileOutputStream(local))
  }

  override def createDirectory(name: String): Unit = {
    file(name).mkdirs
  }

  override def listFiles(dir: String): Seq[FsFile] = {
    val files = file(dir).listFiles()
    if (files != null) {
      FsUtils.sortFiles(files.toIndexedSeq.map(file => new LocalFileSystem.LocalFile(file)))
    }
    else {
      Seq.empty
    }
  }

  override def close(): Unit = {
    // nothing to close
  }

  private def file(name: String): File = {
    new File(fullPath(name))
  }

}
