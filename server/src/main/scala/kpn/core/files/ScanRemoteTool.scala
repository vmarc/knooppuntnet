package kpn.core.files

object ScanRemoteTool {

  def main(args: Array[String]): Unit = {

    val config = FtpConfig(
      host = args(0),
      username = args(1),
      password = args(2)
    )

    new ScanRemoteTool(config).scan(args(3))
  }
}

class ScanRemoteTool(config: FtpConfig) {

  def scan(remoteDir: String): Unit = {
    val remoteFileSystem = new FtpFileSystem(config, "/")
    try {
      scan(remoteFileSystem, remoteDir)
    }
    finally {
      remoteFileSystem.close()
    }
  }

  private def scan(fileSystem: FileSystem, dir: String): Unit = {
    println(dir)
    fileSystem.listFiles(dir).foreach { item =>
      println(FsUtils.withTrailingSlash(dir) + item.name)
    }
  }
}
