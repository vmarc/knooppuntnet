package kpn.core.replicate

import java.io.File

import kpn.core.common.TimestampUtil
import kpn.shared.ReplicationId
import kpn.shared.Timestamp
import org.apache.commons.io.FileUtils

class ReplicationStateRepositoryImpl(replicateDir: File) extends ReplicationStateRepository {

  def write(replicationId: ReplicationId, state: String): Unit = {
    stateDir(replicationId).mkdirs()
    FileUtils.writeStringToFile(stateFile(replicationId), state)
  }

  def read(replicationId: ReplicationId): Timestamp = {
    val state = FileUtils.readFileToString(stateFile(replicationId))
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
