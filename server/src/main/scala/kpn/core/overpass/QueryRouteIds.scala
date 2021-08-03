package kpn.core.overpass

case class QueryRouteIds() extends OverpassQuery {

  def name: String = s"route-ids"

  def string: String = {
    s"relation['type'='route']['network:type'='node_network']['network'];out ids;"
  }
}
