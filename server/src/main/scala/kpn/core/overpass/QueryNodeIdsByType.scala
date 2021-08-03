package kpn.core.overpass

case class QueryNodeIdsByType(nodeTagKey: String) extends OverpassQuery {

  def name: String = s"node-ids-$nodeTagKey"

  def string: String = {
    s"node['network:type'='node_network']['$nodeTagKey'];out ids;"
  }
}
