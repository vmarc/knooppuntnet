package kpn.server.overpass

import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp

trait OverpassRepository {

  def nodeIds(timestamp: Timestamp): Seq[Long]

  def routeIds(timestamp: Timestamp): Seq[Long]

  def networkIds(timestamp: Timestamp): Seq[Long]

  def nodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode]

  def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation]

  def fullRelations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[Relation]

}
