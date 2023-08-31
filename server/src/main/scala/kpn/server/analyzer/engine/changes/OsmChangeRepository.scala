package kpn.server.analyzer.engine.changes

import kpn.api.common.ReplicationId
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.changes.OsmChange

trait OsmChangeRepository {

  def get(replicationId: ReplicationId): OsmChange

  def timestamp(replicationId: ReplicationId): Timestamp
}
