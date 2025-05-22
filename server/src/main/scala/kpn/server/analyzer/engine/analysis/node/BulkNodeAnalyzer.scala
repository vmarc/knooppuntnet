package kpn.server.analyzer.engine.analysis.node

import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.main.NodeMainAnalyzer
import kpn.server.repository.NodeRepository
import kpn.server.repository.RawDataRepository
import org.springframework.stereotype.Component

@Component
class BulkNodeAnalyzer(
  rawDataRepository: RawDataRepository,
  nodeMainAnalyzer: NodeMainAnalyzer,
  nodeRepository: NodeRepository,
) {

  private val BatchSize = 500
  private val log = Log(classOf[BulkNodeAnalyzer])

  def analyze(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    Log.context("nodes") {
      log.info(s"Analyzing ${nodeIds.size} nodes")
      log.infoElapsed {
        val nodeDocs = analyzeNodes(nodeIds)
        nodeRepository.bulkSave(nodeDocs: _*)
        (s"Analyzed ${nodeDocs.size} nodes", nodeDocs)
      }
    }
  }

  private def analyzeNodes(nodeIds: Seq[Long]) = {
    nodeIds.sliding(BatchSize, BatchSize).toSeq.flatMap { batchNodeIds =>
      log.info(s"Analyzing batch $BatchSize nodes")
      val baseNodeDocs = nodeRepository.baseNodesWithIds(batchNodeIds)
      baseNodeDocs.flatMap { baseNodeDoc =>
        Log.context(s"${baseNodeDoc._id}") {
          log.info(s"Analyzing node ${baseNodeDoc._id}")
          nodeMainAnalyzer.analyze(baseNodeDoc) match {
            case Some(nodeDoc) => Some(nodeDoc)
            case None =>
              log.error(s"Could not analyze node ${baseNodeDoc._id}")
              None
          }
        }
      }
    }
  }
}
