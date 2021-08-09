package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.Data
import kpn.server.overpass.OverpassRepository

class OverpassRepositoryMock(beforeData: Data, afterData: Data) extends OverpassRepository {

  val timestampBeforeValue: Timestamp = Timestamp(2015, 8, 11, 0, 0, 1)
  val timestampAfterValue: Timestamp = Timestamp(2015, 8, 11, 0, 0, 4)

  override def nodeIds(timestamp: Timestamp): Seq[Long] = {
    Seq.empty
  }

  override def routeIds(timestamp: Timestamp): Seq[Long] = {
    Seq.empty
  }

  override def networkIds(timestamp: Timestamp): Seq[Long] = {
    Seq.empty
  }

  override def nodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode] = {
    Seq.empty
  }

  override def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation] = {
    Seq.empty
  }

  override def fullRelations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[Relation] = {
    if (timestamp == timestampBeforeValue) {
      relationIds.flatMap(beforeData.relations.get).sortBy(_.id)
    }
    else if (timestamp == timestampAfterValue) {
      relationIds.flatMap(afterData.relations.get).sortBy(_.id)
    }
    else {
      throw new IllegalArgumentException(s"unknown timestamp: ${timestamp.yyyymmddhhmmss}")
    }
  }

}
