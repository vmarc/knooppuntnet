package kpn.core.overpass

case class QueryNodeIds(nodeTagKey: String) extends OverpassQuery {

  def name: String = s"node-ids-$nodeTagKey"

  def string: String = {
    s"node['network:type'='node_network']['$nodeTagKey'];out ids;"
  }
}
