package kpn.server.analyzer.engine.tiles.domain

import kpn.api.custom.Day

case class RouteTileAnalysis(
  routeName: String,
  layer: String,
  surveyDate: Option[Day] = None,
  state: Option[String] = None,
  zoomLevelSegments: Seq[ZoomLevelRouteTileSegments] = Seq.empty
) {
  def sameProperties(other: RouteTileAnalysis): Boolean = {
    routeName == other.routeName &&
      layer == other.layer &&
      surveyDate == other.surveyDate &&
      state == other.state
  }
}
