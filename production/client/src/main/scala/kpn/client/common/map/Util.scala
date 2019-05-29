// TODO migrate to Angular
package kpn.client.common.map

import kpn.shared.LatLon
import kpn.shared.common.TrackPoint

import scala.scalajs.js
import scala.scalajs.js.UndefOr

object Util {

  def toCoordinate(latLon: LatLon): ol.Coordinate = {
    toCoordinate(latLon.lon, latLon.lat)
  }

  def toCoordinate(trackPoint: TrackPoint): ol.Coordinate = {
    toCoordinate(trackPoint.lon, trackPoint.lat)
  }

  def toCoordinate(lon: String, lat: String): ol.Coordinate = {
    toCoordinate(lon.toDouble, lat.toDouble)
  }

  def toCoordinate(lon: Double, lat: Double): ol.Coordinate = {
    ol.proj.proj.fromLonLat(ol.Coordinate(lon.toDouble, lat.toDouble))
  }

  def extent(latLons: Seq[LatLon]): ol.Extent = {

    val lats = latLons.map(_.lat)
    val lons = latLons.map(_.lon)

    val minLat = lats.min
    val maxLat = lats.max
    val minLon = lons.min
    val maxLon = lons.max

    val southWest = toCoordinate(minLon, minLat)
    val northEast = toCoordinate(maxLon, maxLat)

    ol.Extent(southWest, northEast)
  }

  def map(
    layers: Seq[ol.layer.Base],
    target: UndefOr[String] = js.undefined,
    view: ol.View
  ): ol.Map = {

    val attribution = ol.control.Attribution(collapsible = false)
    val controls = ol.control.Control.defaults(olx.control.DefaultsOptions(attribution = false)).extend(js.Array(attribution))

    ol.Map(
      controls = controls,
      layers = layers,
      target = target,
      view = view
    )
  }

}
