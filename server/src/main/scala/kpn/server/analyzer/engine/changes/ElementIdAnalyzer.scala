package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.context.ChangeElementIds

trait ElementIdAnalyzer {
  def routesReferencedBy(elementIds: ChangeElementIds): Set[Long]
}
