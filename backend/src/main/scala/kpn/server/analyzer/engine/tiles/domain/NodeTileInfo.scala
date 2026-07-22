package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.Fact
import kpn.api.common.NodeName
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.api.id.Storable

/*
   Information about node that is needed to build tiles.
 */
case class NodeTileInfo(
  tileName: String,
  nodeId: Long,
  names: Seq[NodeName],
  latitude: String,
  longitude: String,
  lastSurvey: Option[Day],
  tags: Seq[Tag],
  facts: Seq[Fact]
) extends Tagable with Storable
