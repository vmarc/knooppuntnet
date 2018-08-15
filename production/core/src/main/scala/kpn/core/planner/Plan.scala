package kpn.core.planner

trait PlanItem {
  def encoded: Option[String]
}

case class PlanNode(nodeId: Long, nodeName: String, meters: Int) extends PlanItem {
  override def encoded: Option[String] = Some("N" + nodeId)
}

case class PlanIntermediateNode(nodeId: Long, nodeName: String, meters: Int) extends PlanItem {
  override def encoded: Option[String] = Some("I" + nodeId)
}

case class PlanRoute(routeId: Long, routeName: String, meters: Int) extends PlanItem {
  override def encoded: Option[String] = Some("R" + routeId)
}

case class PlanMessageItem(message: PlanMessage) extends PlanItem {
  override def encoded: Option[String] = None
}

trait PlanMessage

case class NodeNotFoundInDatabase(nodeId: Long) extends PlanMessage

case class RouteNotFoundInDatabase(routeId: Long) extends PlanMessage

case class StartNodeNotFoundInGraph(startNodeId: Long, endNodeId: Long) extends PlanMessage

case class EndNodeNotFoundInGraph(startNodeId: Long, endNodeId: Long) extends PlanMessage

case class PathNotFoundInGraph(startNodeId: Long, endNodeId: Long) extends PlanMessage

case class Plan(items: Seq[PlanItem], details: String) {
  def encoded: String = items.flatMap(_.encoded).mkString("-")
}
