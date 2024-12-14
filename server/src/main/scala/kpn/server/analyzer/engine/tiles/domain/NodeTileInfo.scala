package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.Fact
import kpn.api.common.NodeName
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Tag

/*
   Information about node that is needed to build tiles.
 */
case class NodeTileInfo(
  _id: Long,
  names: Seq[NodeName],
  latitude: String,
  longitude: String,
  lastSurvey: Option[Day],
  tags: Seq[Tag],
  facts: Seq[Fact]
) extends Tagable
