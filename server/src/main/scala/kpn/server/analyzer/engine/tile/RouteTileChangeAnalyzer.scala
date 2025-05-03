package kpn.server.analyzer.engine.tile

import kpn.core.doc.BaseRouteDoc

trait RouteTileChangeAnalyzer {

  def impactedTiles(before: BaseRouteDoc, after: BaseRouteDoc): Seq[String]
}
