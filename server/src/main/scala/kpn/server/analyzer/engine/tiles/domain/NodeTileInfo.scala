package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.NodeName
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tags

/*
   Information about node that is needed to build tiles.
 */
case class NodeTileInfo(
  _id: Long,
  names: Seq[NodeName],
  latitude: String,
  longitude: String,
  lastSurvey: Option[Day],
  tags: Tags,
  facts: Seq[Fact]
)
