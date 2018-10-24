package kpn.core.tools.backup

import com.typesafe.config.ConfigFactory
import kpn.core.files.FileSystem
import kpn.core.files.FsUtils
import kpn.core.files.FtpConfig
import kpn.core.files.FtpFileSystem
import kpn.core.files.LocalFileSystem
import kpn.core.util.Log

object BackupTool {

  private val log = Log(classOf[BackupTool])

  def main(args: Array[String]): Unit = {

    val exit = BackupToolOptions.parse(args) match {
      case Some(options) =>

        val localFileSystem = new LocalFileSystem(options.localRoot)

        val remoteFileSystem = {
          val ftpConfig = {
            val config = ConfigFactory.load
            val host = config.getString("kpn-server.backup.host")
            val user = config.getString("kpn-server.backup.user")
            val password = config.getString("kpn-server.backup.password")
            FtpConfig(host, user, password)
          }
          new FtpFileSystem(ftpConfig, options.remoteRoot)
        }

        try {
          new BackupTool(localFileSystem, remoteFileSystem).backup(options.directory)
        }
        catch {
          case e: Throwable => log.fatal("Exception thrown during backup", e)
        }
        finally {
          localFileSystem.close()
          remoteFileSystem.close()
          log.info("Done")
        }
        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }
}

class BackupTool(localFileSystem: FileSystem, remoteFileSystem: FileSystem) {

  def backup(dir: String): Unit = {
    process(dir, 0)
  }

  private def process(dir: String, level: Int): Unit = {

    if (level < 3) {
      BackupTool.log.info("progress " + dir)
    }

    val (localSubDirectories, localFiles) = localFileSystem.listFiles(dir).partition(_.isDirectory)
    val (remoteSubDirectories, remoteFiles) = remoteFileSystem.listFiles(dir).partition(_.isDirectory)

    val remoteSubDirectoryNames = remoteSubDirectories.map(_.name)
    val localSubdirsNotInRemote = localSubDirectories.filterNot(local => remoteSubDirectoryNames.contains(local.name))
    localSubdirsNotInRemote.foreach { localSubdir =>
      val relativePath = FsUtils.withTrailingSlash(dir) + FsUtils.withoutLeadingSlash(localSubdir.name)
      BackupTool.log.info("transfer " + relativePath)
      remoteFileSystem.createDirectory(relativePath)
    }

    if (localFiles.nonEmpty) {
      val remoteFileNames = remoteFiles.map(_.name)
      val localFilesNotInRemote = localFiles.filterNot(localFile => remoteFileNames.contains(localFile.name))
      localFilesNotInRemote.foreach { localFile =>
        val relativePath = dir + "/" + localFile.name
        BackupTool.log.info("transfer " + relativePath)
        remoteFileSystem.putFile(localFile.toFile, relativePath)
      }
    }

    localSubDirectories.foreach(item => process(FsUtils.withTrailingSlash(dir) + item.name, level + 1))
  }
}
