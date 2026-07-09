package kpn.server.analyzer.engine.analysis.common

import kpn.api.common.LatLon
import kpn.api.common.common.TrackPath
import kpn.server.analyzer.engine.tiles.domain.Point

object Converter {

  def trackPathToPoints(trackPath: TrackPath): Seq[Point] = {
    val trackPoints = Seq(trackPath.segments.head.source) ++
      trackPath.segments.flatMap(_.fragments.map(_.trackPoint))
    trackPoints.map(tp => Point(tp.lon.toDouble, tp.lat.toDouble))
  }

  def latLonToPoint(latLon: LatLon): Point = {
    Point(latLon.lon, latLon.lat)
  }

}
