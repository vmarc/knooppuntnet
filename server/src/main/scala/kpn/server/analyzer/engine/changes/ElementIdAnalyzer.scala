package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.context.ChangeElementIds
import kpn.server.analyzer.engine.context.WatchedRoutes

trait ElementIdAnalyzer {
  def referencedBy(watchedRoutes: WatchedRoutes, elementIds: ChangeElementIds): Set[Long]
}
