package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc

trait RouteTileChangeAnalyzer {

  def impactedTiles(before: Seq[RouteTileDoc], after: Seq[RouteTileDoc]): Seq[String]
}
