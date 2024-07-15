package kpn.server.analyzer.engine.tiles.domain

import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Tag
import kpn.core.doc.RouteDetailPath
import kpn.core.doc.RouteDetailSegment
import kpn.core.doc.RouteDetailSegmentElement

/*
  Information about route that is needed to create tiles.
 */
case class RouteTileInfo(
  _id: Long,
  name: String,
  proposed: Boolean,
  lastSurvey: Option[Day],
  tags: Seq[Tag],
  facts: Seq[Fact],
  segments: Seq[RouteDetailSegment],
  segmentElements: Seq[RouteDetailSegmentElement],
  paths: Seq[RouteDetailPath],
) extends Tagable
