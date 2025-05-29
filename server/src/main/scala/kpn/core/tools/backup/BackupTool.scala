package kpn.core.tools.backup

import kpn.core.files.FileSystem
import kpn.core.files.FsUtils
import kpn.core.files.FtpConfig
import kpn.core.files.FtpFileSystem
import kpn.core.files.LocalFileSystem
import kpn.core.util.Log
import kpn.database.base.Exit

object BackupTool {

  private val log = Log(classOf[BackupTool])

  def main(args: Array[String]): Unit = {
    val exitCode = execute(args)
    System.exit(exitCode)
  }

  private def execute(args: Array[String]): Int = {
    try {
      BackupToolOptions.parse(args) match {
        case Some(options) => executeWithOptions(options)
        case None =>
          // arguments are bad, error message will have been displayed
          Exit.Failure
      }
    } catch {
      case e: Exception =>
        log.error(e.getMessage)
        Exit.Failure
    }
  }

  private def executeWithOptions(options: BackupToolOptions): Int = {
    val localFileSystem = new LocalFileSystem(options.localRoot)

    val remoteFileSystem = {
      val ftpConfig = {
        // val config = ConfigFactory.load
        // val host = config.getString("kpn-server.backup.host")
        // val user = config.getString("kpn-server.backup.user")
        // val password = config.getString("kpn-server.backup.password")
        val host = "kpn-server.backup.host"
        val user = "kpn-server.backup.user"
        val password = "kpn-server.backup.password"
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
    Exit.Success
  }
}

class BackupTool(localFileSystem: FileSystem, remoteFileSystem: FileSystem) {

  def backup(dir: String): Unit = {
    process(dir, 0)
  }

  private def process(dir: String, level: Int): Unit = {

    if (level < 3) {
      BackupTool.log.info(s"progress $dir")
    }

    val (localSubDirectories, localFiles) = localFileSystem.listFiles(dir).partition(_.isDirectory)
    val (remoteSubDirectories, remoteFiles) = remoteFileSystem.listFiles(dir).partition(_.isDirectory)

    val remoteSubDirectoryNames = remoteSubDirectories.map(_.name)
    val localSubdirsNotInRemote = localSubDirectories.filterNot(local => remoteSubDirectoryNames.contains(local.name))
    localSubdirsNotInRemote.foreach { localSubdir =>
      val relativePath = s"${FsUtils.withTrailingSlash(dir)}${FsUtils.withoutLeadingSlash(localSubdir.name)}"
      BackupTool.log.info(s"transfer $relativePath")
      remoteFileSystem.createDirectory(relativePath)
    }

    if (localFiles.nonEmpty) {
      val remoteFileNames = remoteFiles.map(_.name)
      val localFilesNotInRemote = localFiles.filterNot(localFile => remoteFileNames.contains(localFile.name))
      localFilesNotInRemote.foreach { localFile =>
        val relativePath = s"$dir/${localFile.name}"
        BackupTool.log.info(s"transfer $relativePath")
        remoteFileSystem.putFile(localFile.toFile, relativePath)
      }
    }

    localSubDirectories.foreach(item => process(s"${FsUtils.withTrailingSlash(dir)}${item.name}", level + 1))
  }
}
