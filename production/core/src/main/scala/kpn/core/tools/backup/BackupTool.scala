package kpn.core.tools.backup

import java.io.PrintWriter
import java.net.InetAddress
import java.nio.file.FileSystems

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.ftp.FtpCredentials.NonAnonFtpCredentials
import akka.stream.alpakka.ftp.FtpSettings
import akka.stream.alpakka.ftp.scaladsl.Ftp
import akka.stream.scaladsl.FileIO
import com.typesafe.config.ConfigFactory
import kpn.core.app.ActorSystemConfig
import org.apache.commons.net.PrintCommandListener
import org.apache.commons.net.ftp.FTPClient

import scala.concurrent.Await
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.Duration

object BackupTool {

  def main(args: Array[String]): Unit = {
    val settings = ftpSettings()
    val system = ActorSystemConfig.actorSystem()

    try {
      new BackupTool(system, settings).transfer(args.toSeq)
    }
    finally {
      Await.result(system.terminate(), Duration.Inf)
      ()
    }
  }

  private def ftpSettings(): FtpSettings = {
    val config = ConfigFactory.load
    val host = config.getString("kpn-server.backup.host")
    val user = config.getString("kpn-server.backup.user")
    val password = config.getString("kpn-server.backup.password")

    FtpSettings(
      InetAddress.getByName(host),
      credentials = NonAnonFtpCredentials(user, password),
      binary = true,
      passiveMode = true,
      configureConnection = (ftpClient: FTPClient) => {
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true))
      }
    )
  }
}

class BackupTool(system: ActorSystem, settings: FtpSettings) {

  private implicit val implicitSystem: ActorSystem = system
  private implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def transfer(fileNames: Seq[String]): Unit = {
    fileNames.foreach { fileName: String =>
      val sourceFileName = "/kpn/cache/" + fileName
      val destinationFileName = "/cache-backup/" + fileName
      val source = FileIO.fromPath(FileSystems.getDefault.getPath(sourceFileName))
      Await.result(Ftp.toPath(destinationFileName, settings).runWith(source), Duration.Inf)
    }
  }
}
