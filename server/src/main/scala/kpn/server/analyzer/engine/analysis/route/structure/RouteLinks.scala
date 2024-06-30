package kpn.server.analyzer.engine.analysis.route.structure

case class RouteLinks(links: Seq[RouteLink]) {
  def routeLinkWays: Seq[RouteLinkWay] = {
    links.flatMap { link =>
      link match {
        case routeLinkWay: RouteLinkWay => Some(routeLinkWay)
        case _ => None
      }
    }
  }
}
