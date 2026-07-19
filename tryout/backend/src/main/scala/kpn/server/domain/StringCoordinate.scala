package kpn.server.domain

import kpn.api.common.LatLon

case class StringCoordinate(x: String, y: String) extends LatLon {
  override def latitude: String = x

  override def longitude: String = y
}
