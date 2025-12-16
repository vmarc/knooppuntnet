package kpn.core.doc

import kpn.api.common.Fact
import kpn.api.common.RouteSummary
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
  summary: RouteSummary,
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

  def toRef: Ref = Ref(_id, summary.name)

  def deactivated: OldRouteDoc = {
    copy(
      active = false,
      labels = labels.filterNot(_.startsWith("fact"))
    )
  }
}
