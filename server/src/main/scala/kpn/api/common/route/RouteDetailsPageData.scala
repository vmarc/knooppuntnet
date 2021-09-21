package kpn.api.common.route

import kpn.api.common.RouteSummary
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class RouteDetailsPageData(
  id: Long,
  active: Boolean,
  summary: RouteSummary,
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
)
