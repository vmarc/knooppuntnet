package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.context.ChangeElementIds
import kpn.server.analyzer.engine.context.ElementIdMap

trait ElementIdAnalyzer {
  def referencedBy(elementIdMap: ElementIdMap, elementIds: ChangeElementIds): Set[Long]
}
