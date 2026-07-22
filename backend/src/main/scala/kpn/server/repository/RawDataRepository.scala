package kpn.server.repository

import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Timestamp
import kpn.core.doc.RawRouteDoc

trait RawDataRepository {
  def nodeIds(timestamp: Timestamp): Seq[Long]

  def nodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode]

  def networkIds(timestamp: Timestamp): Seq[Long]

  def networks(timestamp: Timestamp, networkIds: Seq[Long]): Seq[RawRelation]

  def routeIds(timestamp: Timestamp): Seq[Long]

  def route(timestamp: Timestamp, routeId: Long): Option[RawRouteDoc]
}
