package kpn.server.analyzer.engine.monitor

object MonitorRouteElement {
  def from(fragments: Seq[MonitorRouteFragment]): MonitorRouteElement = {
    val oneWay = fragments.exists(_.oneWay)
    MonitorRouteElement(0, fragments, oneWay)
  }
}

case class MonitorRouteElement(id: Long, fragments: Seq[MonitorRouteFragment], oneWay: Boolean) {

  def startNodeId: Long = {
    fragments.head.startNode.id
  }

  def endNodeId: Long = {
    fragments.last.endNode.id
  }

  def string: String = {
    val endNodeIds = fragments.map(_.endNode.id)
    val nodeString = startNodeId.toString + endNodeIds.mkString(">", ">", "")
    val oneWayString = if (oneWay) " (oneWay)" else ""
    nodeString + oneWayString
  }
}
