package kpn.server.analyzer.engine.elevation

import kpn.core.common.LatLonD
import kpn.core.util.Haversine

import scala.annotation.tailrec

class ElevationCoordinatesBuilder {

  private val segmentDistance = 5

  def build(latLons: Seq[LatLonD]): Seq[LatLonD] = {
    buildCoordinates(segmentDistance, latLons, Seq.empty)
  }

  @tailrec
  private def buildCoordinates(remainingDistanceInSegment: Double, latLons: Seq[LatLonD], result: Seq[LatLonD]): Seq[LatLonD] = {
    if (latLons.size == 1) {
      result ++ latLons
    }
    else {
      val latLon1 = latLons.head
      val latLon2 = latLons(1)
      val segmentLength = distance(latLon1, latLon2)
      if (remainingDistanceInSegment < segmentLength) {
        val ratio = remainingDistanceInSegment / segmentLength
        val newStartLat = latLon1.lat + (ratio * (latLon2.lat - latLon1.lat))
        val newStartLon = latLon1.lon + (ratio * (latLon2.lon - latLon1.lon))
        val newStart = LatLonD(newStartLat, newStartLon)
        val newLatLons = Seq(newStart) ++ latLons.tail
        buildCoordinates(segmentDistance, newLatLons, result :+ newStart)
      }
      else {
        buildCoordinates(remainingDistanceInSegment - segmentLength, latLons.tail, result)
      }
    }
  }

  private def distance(a: LatLonD, b: LatLonD): Double = {
    Haversine.km(a.lat, a.lon, b.lat, b.lon) * 1000
  }

}
