package kpn.core.util

import kpn.api.common.LatLonImpl
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.route.RouteMap
import kpn.server.json.Json

object GeoJsonUtil {

  def makeGeojson(name: String, trackSegment: TrackSegment): Unit = {
    val latlons = trackSegment.trackPoints.map(point => LatLonImpl(point.lat, point.lon))
    val coordinates = latlons.toArray.map(c => Array(c.lon, c.lat))
    val line = GeoJsonLineStringGeometry(
      "LineString",
      coordinates
    )
    val json = Json.objectMapper.writerWithDefaultPrettyPrinter()
    println(s"https://geojson.io/ $name")
    println(json.writeValueAsString(line))
  }

  def printMap(map: RouteMap): Unit = {

    val colors = new Colors()

    val features = GeoJson.featureCollection(
      Seq(
        map.forwardPath.toSeq.map { path =>
          trackPathFeature("forward", "green", path)
        },
        map.backwardPath.toSeq.map { path =>
          trackPathFeature("backward", "red", path)
        },
        map.startTentaclePaths.zipWithIndex.map { case (path, index) =>
          trackPathFeature(s"startTentacle ${index + 1}", colors.next(), path)
        },
        map.endTentaclePaths.zipWithIndex.map { case (path, index) =>
          trackPathFeature(s"endTentacle ${index + 1}", colors.next(), path)
        },
        map.unusedSegments.zipWithIndex.map { case (segment, index) =>
          trackSegmentFeature(s"unused segment ${index + 1}", colors.next(), segment)
        }
      ).flatten
    )

    val json = Json.objectMapper.writerWithDefaultPrettyPrinter()
    println("https://geojson.io/")
    println(json.writeValueAsString(features))
  }

  def trackSegmentFeature(name: String, color: String, trackSegment: TrackSegment): GeoJsonFeature = {
    fromTrackPoints(name, color, trackSegment.trackPoints)
  }

  def trackPathFeature(name: String, color: String, trackPath: TrackPath): GeoJsonFeature = {
    fromTrackPoints(name, color, trackPath.trackPoints)
  }

  def fromTrackPoints(name: String, color: String, trackPoints: Seq[TrackPoint]): GeoJsonFeature = {
    val latlons = trackPoints.map(point => LatLonImpl(point.lat, point.lon))
    val coordinates = latlons.toArray.map(c => Array(c.lon, c.lat))
    val line = GeoJsonLineStringGeometry(
      "LineString",
      coordinates
    )
    GeoJson.feature(
      line,
      GeoJsonProperties(
        name = Some(name),
        stroke = Some(color)
      )
    )
  }
}
