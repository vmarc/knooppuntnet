package kpn.core.overpass

case class QueryRouteIds() extends OverpassQuery {

  def name: String = s"route-ids"

  def string: String = {
    s"relation['network:type'='node_network']['type'='route']['network'];out ids;"
  }
}
