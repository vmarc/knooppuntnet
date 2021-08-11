package kpn.core.util

import kpn.api.common.LatLon
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point

import scala.math.asin
import scala.math.cos
import scala.math.pow
import scala.math.sin
import scala.math.sqrt

// https://rosettacode.org/wiki/Haversine_formula#Scala

object Haversine {

  val R = 6372.8 //radius in km

  def distance(line: Line): Double = {
    distance(line.p1, line.p2)
  }

  def distance(p1: Point, p2: Point): Double = {
    km(p1.x, p1.y, p2.x, p2.y) * 1000
  }

  def km(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double = {
    val dLat = (lat2 - lat1).toRadians
    val dLon = (lon2 - lon1).toRadians
    val a = pow(sin(dLat / 2), 2) + pow(sin(dLon / 2), 2) * cos(lat1.toRadians) * cos(lat2.toRadians)
    val c = 2 * asin(sqrt(a))
    R * c
  }

  def meters(nodes: Seq[LatLon]): Int = {
    if (nodes.size < 2) {
      0
    }
    else {
      val km = nodes.sliding(2).map { case Seq(node1, node2) =>
        Haversine.km(node1.lat, node1.lon, node2.lat, node2.lon)
      }.sum
      (km * 1000).toInt
    }
  }
}
