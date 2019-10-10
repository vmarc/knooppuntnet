package kpn.core.replicate

import java.io.File

import kpn.core.engine.changes.MinuteDiffReader
import kpn.shared.ReplicationId

object VerifyZipFiles {
  def main(args: Array[String]): Unit = {
    println("Start VerifyZipFiles")
    val reader = new MinuteDiffReader(new File("/kpn/replicate"))
    (1916075 to 1923224) foreach { replicationNumber =>
      val replicationId = ReplicationId(replicationNumber)
      try {
        reader.read(replicationId)
      }
      catch {
        case e: java.io.EOFException =>
          println("Error reading " + replicationId.name)
      }
    }
    println("End VerifyZipFiles")
  }
}
