package kpn.server.analyzer.engine.analysis.network.info.domain

import kpn.api.common.Fact
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

// TODO redesign - cleanup?
case class NetworkRouteDetail(
  id: Long,
  name: String,
  length: Long,
  facts: Seq[Fact],
  proposed: Boolean,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  role: Option[String],
  tags: Seq[Tag],
  nodeRefs: Seq[Long]
)
