package kpn.core.files

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

import kpn.core.util.Log
import org.apache.commons.net.ProtocolCommandEvent
import org.apache.commons.net.ProtocolCommandListener
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import org.apache.commons.net.ftp.FTPReply

object FtpFileSystem {

  private class FtpFile(file: FTPFile) extends FsFile {

    override def isFile: Boolean = file.isFile

    override def isDirectory: Boolean = file.isDirectory

    override def name: String = file.getName

    override def toFile: File = {
      throw new UnsupportedOperationException()
    }

  }

  private class FtpLog(log: Log) extends ProtocolCommandListener {

    override def protocolCommandSent(event: ProtocolCommandEvent): Unit = {
      if (event.getCommand == "PASS") {
        log.debug("> PASS ...") // do not show password in log file
      }
      else {
        log.debug("> " + toMessage(event))
      }
    }

    override def protocolReplyReceived(event: ProtocolCommandEvent): Unit = {
      log.debug("< " + toMessage(event))
    }

    private def toMessage(event: ProtocolCommandEvent): String = {
      event.getMessage.filterNot(_ == '\n')
    }
  }

}

class FtpFileSystem(ftpConfig: FtpConfig, val baseDir: String) extends FileSystem {

  private val log = Log(classOf[FtpFileSystem])

  private val client: FTPClient = {
    val newClient = new FTPClient()
    newClient.addProtocolCommandListener(new FtpFileSystem.FtpLog(log))
    newClient.connect(ftpConfig.host)
    val reply: Int = newClient.getReplyCode
    if (!FTPReply.isPositiveCompletion(reply)) {
      newClient.disconnect()
      throw new IOException("FTP server refused connection.")
    }
    val login: Boolean = newClient.login(ftpConfig.username, ftpConfig.password)
    if (!login) {
      throw new FtpException("Could not login", newClient.getReplyStrings)
    }
    newClient.setFileType(FTP.BINARY_FILE_TYPE)
    newClient.setControlKeepAliveTimeout(300)
    newClient
  }

  override def deleteFile(name: String): Unit = {
    val filename = fullPath(name)
    if (!client.deleteFile(filename)) {
      throw new FtpException("Could not delete file " + filename, client.getReplyStrings)
    }
  }

  override def deleteDirectory(name: String): Unit = {
    val filename = fullPath(name)
    if (!client.removeDirectory(filename)) {
      throw new FtpException("Could not delete directory " + filename, client.getReplyStrings)
    }
  }

  override def putFile(local: File, dest: String): Unit = {
    val targetFilename = fullPath(dest)
    val in = new FileInputStream(local)
    try {
      if (!client.storeFile(targetFilename, in)) {
        throw new FtpException("Could not put file " + local + " to " + targetFilename, client.getReplyStrings)
      }
    }
    finally {
      if (in != null) {
        in.close()
      }
    }
  }

  override def retrieveFile(local: File, dest: String): Unit = {
    val targetFilename = fullPath(dest)
    val out = new FileOutputStream(local)
    try {
      if (!client.retrieveFile(targetFilename, out)) {
        throw new FtpException("Could not get file " + local + " from " + targetFilename, client.getReplyStrings)
      }
    }
    finally {
      if (out != null) {
        out.close()
      }
    }
  }

  override def createDirectory(name: String): Unit = {
    val fileName = fullPath(name)
    if (!client.makeDirectory(fileName)) {
      throw new FtpException("Could not create directory " + fileName, client.getReplyStrings)
    }
  }

  override def listFiles(dir: String): Seq[FsFile] = {
    FsUtils.sortFiles(client.listFiles(fullPath(dir)).toIndexedSeq.map(file => new FtpFileSystem.FtpFile(file)))
  }

  override def close(): Unit = {
    client.disconnect()
  }
}
