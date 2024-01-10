package kpn.server.analyzer.engine.monitor

object MonitorRouteElement {
  def from(fragments: Seq[MonitorRouteFragment]): MonitorRouteElement = {
    val oneWay = fragments.exists(_.oneWay)
    MonitorRouteElement(fragments, oneWay)
  }
}

case class MonitorRouteElement(fragments: Seq[MonitorRouteFragment], oneWay: Boolean) {

  def string: String = {
    val startNodeId = fragments.head.startNode.id
    val endNodeIds = fragments.map(_.endNode.id)
    val nodeString = startNodeId.toString + endNodeIds.mkString(">", ">", "")
    val oneWayString = if (oneWay) " (oneWay)" else ""
    nodeString + oneWayString
  }
}
