package kpn.server.analyzer.engine.monitor.structure

object MonitorRouteElement {
  def from(fragments: Seq[StructureFragment], direction: Option[ElementDirection.Value]): MonitorRouteElement = {
    MonitorRouteElement(0, fragments, direction)
  }
}

case class MonitorRouteElement(
  id: Long,
  fragments: Seq[StructureFragment],
  direction: Option[ElementDirection.Value]
) {

  def startNodeId: Long = {
    fragments.head.startNode.id
  }

  def endNodeId: Long = {
    fragments.last.endNode.id
  }

  def isLoop: Boolean = {
    startNodeId == endNodeId
  }

  def string: String = {
    val endNodeIds = fragments.map(_.endNode.id)
    val nodeString = startNodeId.toString + endNodeIds.mkString(">", ">", "")
    val directionString = direction match {
      case None => ""
      case Some(string) => s" ($string)"
    }
    nodeString + directionString
  }
}
