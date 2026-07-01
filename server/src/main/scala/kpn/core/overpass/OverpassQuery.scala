package kpn.core.overpass

trait OverpassQuery {

  def timeout: Option[Long] = None

  def maxSize: Option[Long] = None

  def string: String

  def name: String

  def detailString: String = name
}
