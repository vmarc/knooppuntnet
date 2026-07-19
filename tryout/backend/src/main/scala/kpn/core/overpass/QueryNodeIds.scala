package kpn.core.overpass

case class QueryNodeIds() extends OverpassQuery {

  def name: String = s"node-ids"

  def string: String = {
    s"node['network:type'='node_network'];out ids;"
  }
}
