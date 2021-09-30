package kpn.server.analyzer.engine.analysis.node

import kpn.api.custom.Timestamp
import kpn.database.base.Database
import kpn.core.doc.NodeDoc
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.overpass.OverpassRepository
import org.springframework.stereotype.Component

@Component
class BulkNodeAnalyzerImpl(
  database: Database,
  overpassRepository: OverpassRepository,
  nodeAnalyzer: NodeAnalyzer
) extends BulkNodeAnalyzer {

  def analyze(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[NodeDoc] = {
    val rawNodes = overpassRepository.nodes(timestamp, nodeIds)
    val nodeDocs = rawNodes.flatMap { rawNode =>
      nodeAnalyzer.analyze(NodeAnalysis(rawNode)).map(_.toNodeDoc)
    }
    if (nodeDocs.nonEmpty) {
      database.nodes.bulkSave(nodeDocs)
    }
    nodeDocs
  }
}
