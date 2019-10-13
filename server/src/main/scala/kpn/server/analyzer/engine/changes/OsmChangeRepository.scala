package kpn.server.analyzer.engine.changes

import java.io.File

import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.shared.ReplicationId
import kpn.shared.Timestamp

class OsmChangeRepository(replicateDir: File) {

  def get(replicationId: ReplicationId): OsmChange = {
    val fileName = replicateDir.getAbsolutePath + "/" + replicationId.name + ".osc.gz"
    new OsmChangeReader(fileName).read
  }

  def timestamp(replicationId: ReplicationId): Timestamp = {
    new ReplicationStateReader(replicateDir).readTimestamp(replicationId).get
  }
}
