package kpn.server.overpass

import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp

trait OverpassRepository {

  def relations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[RawRelation]

  def fullRelations(timestamp: Timestamp, relationIds: Seq[Long]): Seq[Relation]

  def routeIds(timestamp: Timestamp): Seq[Long]

  def nodeIds(timestamp: Timestamp): Seq[Long]

}
