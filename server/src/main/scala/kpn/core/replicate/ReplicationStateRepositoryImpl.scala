package kpn.core.replicate

import java.io.File

import kpn.api.common.ReplicationId
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil
import org.apache.commons.io.FileUtils

class ReplicationStateRepositoryImpl(replicateDir: File) extends ReplicationStateRepository {

  def write(replicationId: ReplicationId, state: String): Unit = {
    stateDir(replicationId).mkdirs()
    FileUtils.writeStringToFile(stateFile(replicationId), state, "UTF-8")
  }

  def read(replicationId: ReplicationId): Timestamp = {
    val state = FileUtils.readFileToString(stateFile(replicationId), "UTF-8")
    val lines = state.split("\n")
    lines.find(_.startsWith("timestamp=")) match {
      case Some(line) =>
        val timestamp = line.drop("timestamp=".length).replaceAll("\\\\:", ":")
        TimestampUtil.parseIso(timestamp)
      case None => throw new RuntimeException("Timestamp line not found in:\n" + state)
    }
  }

  private def stateFile(replicationId: ReplicationId): File = new File(replicateDir, replicationId.name + ".state.txt")

  private def stateDir(replicationId: ReplicationId): File = {
    new File(replicateDir, "%03d/%03d".format(replicationId.level1, replicationId.level2))
  }
}
