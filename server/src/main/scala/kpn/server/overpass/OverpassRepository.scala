package kpn.server.overpass

import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp

trait OverpassRepository {

  def nodeIds(timestamp: Timestamp): Seq[Long]

  def routeIds(timestamp: Timestamp): Seq[Long]

  def networkIds(timestamp: Timestamp): Seq[Long]

  def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation]

  def fullRelations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[Relation]

}
