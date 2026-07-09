package kpn.core.tools.next.support

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import kpn.core.tools.config.Dirs
import org.apache.commons.io.IOUtils

import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream

object TryoutReadReplicateTool {
  def main(args: Array[String]): Unit = {
    new TryoutReadReplicateTool().tryout()
  }
}

class TryoutReadReplicateTool {
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
    val filename = s"${Dirs.root}/status/analysis2"
    val inputStream = channel.get(filename)
    val string = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    println(string)
  }

  private def readStateFile(channel: ChannelSftp): Unit = {
    val filename = s"${Dirs.root}/replicate/005/981/121.state.txt"
    val inputStream = channel.get(filename)
    val string = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    println("---")
    println(string)
    println("---")
  }

  private def readMinuteDiffFile(channel: ChannelSftp): Unit = {
    val filename = s"${Dirs.root}/replicate/005/981/121.osc.gz"
    val gzippedInputStream = channel.get(filename)
    val inputStream = new GZIPInputStream(gzippedInputStream)
    val string = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    println(string)
  }
}
