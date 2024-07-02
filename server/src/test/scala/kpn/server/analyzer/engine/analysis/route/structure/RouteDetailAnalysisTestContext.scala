package kpn.server.analyzer.engine.analysis.route.structure

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

case class RouteDetailAnalysisTestContext(context: RouteDetailAnalysisContext) {
  def links = context.links.links.zipWithIndex.map { case (link, index) =>
    s"${index + 1}    ${link.linkDetail}"
  }
}
