package kpn.core.doc

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.context.ElementIds

case class OldRouteDoc(
  _id: Long, // routeId
  active: Boolean,
  labels: Seq[String],
  countries: Seq[Country],
  nodeNetwork: Boolean,
  routeTypes: Seq[RouteType],
  scopes: Seq[RouteScope],
  name: String,
  meters: Long,
  wayCount: Long,
  proposed: Boolean,
  version: Long,
  changeSetId: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact],
  oldFacts: Seq[Fact],
  analysis: RouteInfoAnalysis,
  tiles: Seq[String],
  nodeRefs: Seq[Long],
  elementIds: ElementIds,
  edges: Seq[RouteEdge],
) extends WithId {

  def toRef: Ref = Ref(_id, name)

  def deactivated: OldRouteDoc = {
    copy(
      active = false,
      labels = labels.filterNot(_.startsWith("fact"))
    )
  }
}
