package kpn.core.overpass

case class QueryNetworkIds() extends OverpassQuery {

  def name: String = s"network-ids"

  def string: String = {
    s"relation['network:type'='node_network']['type'='network']['network'];out ids;"
  }
}
