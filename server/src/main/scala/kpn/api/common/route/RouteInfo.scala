package kpn.api.common.route

import kpn.api.base.WithId
import kpn.api.common.RouteSummary
import kpn.api.common.common.Ref
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.mongo.doc.Label
import kpn.server.analyzer.engine.context.ElementIds

case class RouteInfo(
  _id: Long, // routeId
  labels: Seq[String],
  summary: RouteSummary,
  active: Boolean,
  proposed: Boolean,
  version: Long,
  changeSetId: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  tags: Tags,
  facts: Seq[Fact],
  analysis: RouteInfoAnalysis,
  tiles: Seq[String],
  nodeRefs: Seq[Long],
  elementIds: ElementIds,
  edges: Seq[RouteEdge]
) extends Tagable with WithId {

  def id: Long = summary.id

  def toRef: Ref = Ref(summary.id, summary.name)

  def deactivated: RouteInfo = {
    copy(
      labels = labels.filterNot(_ == Label.active),
      active = false
    )
  }
}
