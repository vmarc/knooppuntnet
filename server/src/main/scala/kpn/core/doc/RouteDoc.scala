package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.RouteSummary
import kpn.api.common.common.Ref
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteNodes
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.RouteMemberInfo
import kpn.api.custom.Timestamp

case class RouteDoc(
  _id: Long, // routeId
  labels: Seq[String],
  summary: RouteSummary,
  proposed: Boolean,
  version: Long,
  changeSetId: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact],
  oldFacts: Seq[Fact],
  unexpectedNodeIds: Seq[Long],
  unexpectedRelationIds: Seq[Long],
  members: Seq[RouteMemberInfo],
  nameDerivedFromNodes: Boolean,
  nodes: RouteNodes,
  analysis: RouteInfoAnalysis,
  segments: Seq[RouteSegment],
  paths: Seq[RoutePath],
) extends WithId {

  def id: Long = summary.id

  def toRef: Ref = Ref(summary.id, summary.name)

  def deactivated: RouteDoc = {
    copy(
      labels = labels.filterNot(label =>
        label == Label.active || label.startsWith("fact")
      )
    )
  }

  def isActive: Boolean = labels.contains(Label.active)
}
