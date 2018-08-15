package kpn.shared

trait LatLon {

  def latitude: String

  def longitude: String

  def lat: Double = latitude.toDouble

  def lon: Double = longitude.toDouble
}
