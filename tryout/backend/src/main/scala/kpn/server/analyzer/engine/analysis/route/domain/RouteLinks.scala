package kpn.server.analyzer.engine.analysis.route.domain

case class RouteLinks(links: Seq[RouteLink]) {
  def routeLinkWays: Seq[RouteLinkWay] = {
    links.flatMap {
      case routeLinkWay: RouteLinkWay => Some(routeLinkWay)
      case _ => None
    }
  }

  def routeLinkNodes: Seq[RouteLinkNode] = {
    links.flatMap {
      case routeLinkNode: RouteLinkNode => Some(routeLinkNode)
      case _ => None
    }
  }
}
