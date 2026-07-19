package kpn.core.overpass

case class QueryNode(nodeId: Long) extends OverpassQuery {

  def name: String = s"node-$nodeId"

  def string: String = s"node($nodeId);out meta;"
}
