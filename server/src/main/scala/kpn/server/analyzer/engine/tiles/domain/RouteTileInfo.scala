package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.common.TrackPath
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tags

/*
  Information about route that is needed to create tiles.
 */
case class RouteTileInfo(
  _id: Long,
  name: String,
  proposed: Boolean,
  lastSurvey: Option[Day],
  tags: Tags,
  facts: Seq[Fact],
  freePaths: Seq[TrackPath],
  forwardPath: Option[TrackPath],
  backwardPath: Option[TrackPath],
  startTentaclePaths: Seq[TrackPath],
  endTentaclePaths: Seq[TrackPath]
)
