package kpn.core.files

import java.io._

object FsUtils {

  def copy(in: InputStream, out: OutputStream): Unit = {
    val bufferedIn = new BufferedInputStream(in)
    val bufferedOut = new BufferedOutputStream(out)
    try {
      val buf = new Array[Byte](1000)
      var read = 0
      do {
        read = bufferedIn.read(buf)
        if (read > 0) {
          bufferedOut.write(buf, 0, read)
        }
      } while (read > 0)
    } finally {
      if (bufferedIn != null) bufferedIn.close()
      if (bufferedOut != null) bufferedOut.close()
    }
  }

  def withTrailingSlash(path: String): String = {
    if (path.endsWith("/")) {
      path
    }
    else {
      path + "/"
    }
  }

  def withoutLeadingSlash(path: String): String = {
    if (path.startsWith("/")) {
      path.tail
    }
    else {
      path
    }
  }

  def sortFiles(fsFiles: Seq[FsFile]): Seq[FsFile] = {
    val filteredFiles = fsFiles.filterNot(fsFile => fsFile.isDirectory && (fsFile.name.equals(".") || fsFile.name.equals("..")))
    filteredFiles.sortWith { (a, b) =>
      if ((a.isDirectory && b.isDirectory) || (a.isFile && b.isFile)) {
        a.name < b.name
      }
      else {
        // directories first
        a.isDirectory && b.isFile
      }
    }
  }

}
