package kpn.api.common.route

import kpn.api.base.WithId
import kpn.api.common.RouteSummary
import kpn.api.common.common.Ref
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class RouteInfo(
  _id: Long, // routeId
  labels: Seq[String],
  summary: RouteSummary,
  active: Boolean,
  orphan: Boolean,
  proposed: Boolean,
  version: Long,
  changeSetId: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  tags: Tags,
  facts: Seq[Fact],
  analysis: RouteInfoAnalysis,
  tiles: Seq[String],
  nodeRefs: Seq[Long]
) extends Tagable with WithId {

  def id: Long = summary.id

  def toRef: Ref = Ref(summary.id, summary.name)

}
