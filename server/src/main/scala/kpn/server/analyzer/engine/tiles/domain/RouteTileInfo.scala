package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.Fact
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.core.doc.BaseRoutePath
import kpn.core.doc.BaseRouteSegment
import kpn.core.doc.BaseRouteSegmentElement

/*
  Information about route that is needed to create tiles.
 */
case class RouteTileInfo(
  _id: Long,
  name: String,
  nodeNetwork: Boolean,
  scopes: Seq[String],
  proposed: Boolean,
  lastSurvey: Option[Day],
  tags: Seq[Tag],
  facts: Seq[Fact],
  segments: Seq[BaseRouteSegment],
  segmentElements: Seq[BaseRouteSegmentElement],
  paths: Seq[BaseRoutePath],
  tiles: Seq[String]
) extends Tagable
