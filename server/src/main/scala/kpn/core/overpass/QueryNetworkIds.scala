package kpn.core.overpass

case class QueryNetworkIds() extends OverpassQuery {

  def name: String = s"network-ids"

  def string: String = {
    s"relation['type'='network']['network:type'='node_network']['network'];out ids;"
  }
}
