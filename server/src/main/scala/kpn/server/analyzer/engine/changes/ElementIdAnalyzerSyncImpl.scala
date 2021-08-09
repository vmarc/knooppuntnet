package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.changes.changes.ElementIds

class ElementIdAnalyzerSyncImpl extends ElementIdAnalyzer {

  def referencedBy(elementIdMap: ElementIdMap, elementIds: ElementIds): Set[Long] = {
    elementIdMap.referencedBy(elementIds)
  }

}
