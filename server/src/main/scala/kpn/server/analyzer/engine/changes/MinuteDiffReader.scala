package kpn.server.analyzer.engine.changes

import java.io.File

import kpn.api.common.ReplicationId
import kpn.core.util.GZipFile
import kpn.core.util.Log

class MinuteDiffReader(replicationDir: File) {

  private val log = Log(classOf[MinuteDiffReader])

  def read(replicationId: ReplicationId): Option[String] = {
    val filename = replicationDir.getAbsolutePath + "/" + replicationId.name + ".osc.gz"
    val file = new File(filename)
    if (file.exists) {
      val contents = GZipFile.read(filename)
      // TODO check contents + log error
      log.debug(s"${replicationId.name}) $filename xml-size=${contents.length}")
      Some(contents)
    }
    else {
      log.debug(s"${replicationId.name}) $filename does not exist")
      None
    }
  }
}
