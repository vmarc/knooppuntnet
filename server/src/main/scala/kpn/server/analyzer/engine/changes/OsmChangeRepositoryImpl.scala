package kpn.server.analyzer.engine.changes

import kpn.api.common.ReplicationId
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.changes.OsmChange

import java.io.File

class OsmChangeRepositoryImpl(replicateDir: File) extends OsmChangeRepository {

  override def get(replicationId: ReplicationId): OsmChange = {
    val fileName = replicateDir.getAbsolutePath + "/" + replicationId.name + ".osc.gz"
    new OsmChangeReader(fileName).read
  }

  override def timestamp(replicationId: ReplicationId): Timestamp = {
    new ReplicationStateReader(replicateDir).readTimestamp(replicationId).get
  }
}
