package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Way
import kpn.core.analysis.Link
import kpn.core.analysis.LinkDirection

case class RouteLinkWay(
  id: Long,
  link: Link,
  role: Option[String],
  way: Way,
  pathIds: Seq[Long]
) extends RouteLink {

  override def idString: String = id.toString

  def linkName: String = link.name

  def linkDetail: String = link.reportString

  def fromNodeId = {
    if (link.direction == LinkDirection.Backward) {
      way.nodeIds.last
    }
    else {
      way.nodeIds.head
    }
  }

  def toNodeId = {
    if (link.direction == LinkDirection.Backward) {
      way.nodeIds.head
    }
    else {
      way.nodeIds.last
    }
  }

  def nodeIds: Seq[Long] = {
    if (link.direction == LinkDirection.Backward) {
      way.nodeIds.reverse
    }
    else {
      way.nodeIds
    }
  }
}
