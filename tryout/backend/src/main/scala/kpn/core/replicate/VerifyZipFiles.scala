package kpn.core.replicate

import kpn.api.common.ReplicationId
import kpn.core.tools.config.Dirs
import kpn.server.analyzer.engine.changes.MinuteDiffReader

import java.io.File

object VerifyZipFiles {
  def main(args: Array[String]): Unit = {
    println("Start VerifyZipFiles")
    val reader = new MinuteDiffReader(new File(Dirs.root, "replicate"))
    (1916075 to 1923224) foreach { replicationNumber =>
      val replicationId = ReplicationId(replicationNumber)
      try {
        reader.read(replicationId)
      }
      catch {
        case _: java.io.EOFException =>
          println(s"Error reading ${replicationId.name}")
      }
    }
    println("End VerifyZipFiles")
  }
}
