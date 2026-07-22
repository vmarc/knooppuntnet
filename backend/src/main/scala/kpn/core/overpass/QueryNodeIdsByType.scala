package kpn.core.overpass

case class QueryNodeIdsByType(nodeTagKey: String) extends OverpassQuery {

  override def timeout: Option[Long] = Some(1500)

  override def maxSize: Option[Long] = Some(24000000000L)

  def name: String = s"node-ids-$nodeTagKey"

  def string: String = {
    s"node['network:type'='node_network']['$nodeTagKey'];out ids;"
  }
}
