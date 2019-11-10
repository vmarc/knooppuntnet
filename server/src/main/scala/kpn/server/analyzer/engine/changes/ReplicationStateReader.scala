package kpn.server.analyzer.engine.changes

import java.io.File

import kpn.api.common.ReplicationId
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil
import kpn.core.util.Log

import scala.io.Source

class ReplicationStateReader(replicationDir: File) {

  private val log = Log(classOf[ReplicationStateReader])

  def readTimestamp(replicationId: ReplicationId): Option[Timestamp] = {
    val filename = replicationDir.getAbsolutePath + "/" + replicationId.name + ".state.txt"
    val file = new File(filename)
    if (file.exists) {
      val lines = Source.fromFile(new File(filename)).getLines().toSeq
      if (lines.size < 5) {
        log.debug(s"$filename contains less than 5 lines")
        None
      }
      else {
        lines.find(_.startsWith("timestamp=")) map {
          line =>
            val utc = line.drop("timestamp=".length).replaceAll("\\\\:", ":")
            val timestamp = TimestampUtil.parseIso(utc)
            //noinspection SideEffectsInMonadicTransformation
            log.debug(s"$filename timestamp=${TimestampUtil.toLocal(timestamp).yyyymmddhhmmss}")
            timestamp
        }
      }
    }
    else {
      log.debug(s"$filename does not exist")
      None
    }
  }
}
