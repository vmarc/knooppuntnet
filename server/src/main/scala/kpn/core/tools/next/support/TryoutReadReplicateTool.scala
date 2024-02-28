package kpn.core.tools.next.support

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import org.apache.commons.io.IOUtils

import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream

object MonitorReadReplicateTool {
  def main(args: Array[String]): Unit = {
    new MonitorReadReplicateTool().tryout()
  }
}

class MonitorReadReplicateTool {
  def tryout(): Unit = {
    val session = openSession()
    try {
      val channel = openChannel(session)
      try {
        readStatusFile(channel)
        readStateFile(channel)
        readMinuteDiffFile(channel)
      }
      finally {
        channel.exit()
      }
    }
    finally {
      session.disconnect()
    }
  }

  private def openSession(): Session = {
    val jsch = new JSch()
    jsch.addIdentity("/home/XXX/.ssh/id_rsa")
    jsch.setKnownHosts("/home/XXX/.ssh/known_hosts")
    val session = jsch.getSession("XXX", "kpn-analysis")
    session.connect()
    session
  }

  private def openChannel(session: Session): ChannelSftp = {
    val channel = session.openChannel("sftp").asInstanceOf[ChannelSftp]
    channel.connect()
    channel
  }

  private def readStatusFile(channel: ChannelSftp): Unit = {
    val inputStream = channel.get("/kpn/status/analysis2")
    val string = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    println(string)
  }

  private def readStateFile(channel: ChannelSftp): Unit = {
    val inputStream = channel.get("/kpn/replicate/005/981/121.state.txt")
    val string = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    println("---")
    println(string)
    println("---")
  }

  private def readMinuteDiffFile(channel: ChannelSftp): Unit = {
    val gzippedInputStream = channel.get("/kpn/replicate/005/981/121.osc.gz")
    val inputStream = new GZIPInputStream(gzippedInputStream)
    val string = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    println(string)
  }
}
