package kpn.server.analyzer.engine.analysis.network.info.domain

import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class NetworkRouteDetail(
  id: Long,
  name: String,
  length: Long,
  facts: Seq[Fact],
  proposed: Boolean,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  role: Option[String],
  tags: Tags,
  nodeRefs: Seq[Long]
)
