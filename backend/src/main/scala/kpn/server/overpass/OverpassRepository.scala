package kpn.server.overpass

import kpn.api.common.Relation
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Timestamp
import kpn.core.doc.RouteRelation

trait OverpassRepository {

  def nodeIds(timestamp: Timestamp): Seq[Long]

  def networkIds(timestamp: Timestamp): Seq[Long]

  def nodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode]

  def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation]

  def fullRelations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[Relation]

  def relationTopLevel(timestamp: Timestamp, relationId: Long): Option[Relation]

  def subRelationTree(timestamp: Timestamp, relationId: Long): Option[RouteRelation]

  def routeIds(timestamp: Timestamp, typeValue: String): Seq[Long]
}
