package kpn.server.analyzer.engine.changes

import java.io.File

import kpn.api.common.ReplicationId
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.changes.OsmChange

class OsmChangeRepository(replicateDir: File) {

  def get(replicationId: ReplicationId): OsmChange = {
    val fileName = replicateDir.getAbsolutePath + "/" + replicationId.name + ".osc.gz"
    new OsmChangeReader(fileName).read
  }

  def timestamp(replicationId: ReplicationId): Timestamp = {
    new ReplicationStateReader(replicateDir).readTimestamp(replicationId).get
  }
}
