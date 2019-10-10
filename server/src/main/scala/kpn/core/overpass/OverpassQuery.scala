package kpn.core.overpass

trait OverpassQuery {
  def string: String
  def name: String
  def detailString: String = name
}
