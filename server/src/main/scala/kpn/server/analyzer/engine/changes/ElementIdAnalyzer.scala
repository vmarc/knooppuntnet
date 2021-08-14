package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.context.ElementIdMap
import kpn.server.analyzer.engine.context.ElementIds

trait ElementIdAnalyzer {
  def referencedBy(elementIdMap: ElementIdMap, elementIds: ElementIds): Set[Long]
}
