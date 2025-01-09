package kpn.server.analyzer.engine.analysis.node

import kpn.api.custom.Timestamp
import kpn.core.doc.NodeDoc
import org.springframework.stereotype.Component

@Component
class BulkNodeAnalyzerImpl(
  //  database: Database,
  //  overpassRepository: OverpassRepository,
  //  nodeAnalyzer: NodeAnalyzer,
  //  nodeMainAnalyzer: NodeMainAnalyzer
) extends BulkNodeAnalyzer {

  def analyze(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[NodeDoc] = {
    throw new RuntimeException("implementation missing")
    //    val baseNodes = ...
    //    val nodeDocs = rawNodes.flatMap { rawNode =>
    //      nodeMainAnalyzer.analyze(baseNodeDoc)
    //    }
    //    if (nodeDocs.nonEmpty) {
    //      database.nodes.bulkSave(nodeDocs)
    //    }
    //    nodeDocs
    Seq.empty
  }
}
