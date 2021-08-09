package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.changes.changes.ElementIds

trait ElementIdAnalyzer {
  def referencedBy(elementIdMap: ElementIdMap, elementIds: ElementIds): Set[Long]
}
