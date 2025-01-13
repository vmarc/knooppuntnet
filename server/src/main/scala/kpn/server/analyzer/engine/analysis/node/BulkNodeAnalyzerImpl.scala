package kpn.server.analyzer.engine.analysis.node

import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.main.NodeMainAnalyzer
import kpn.server.repository.NodeRepository
import kpn.server.repository.RawDataRepository
import org.springframework.stereotype.Component

@Component
class BulkNodeAnalyzerImpl(
  rawDataRepository: RawDataRepository,
  nodeMainAnalyzer: NodeMainAnalyzer,
  nodeRepository: NodeRepository,
) extends BulkNodeAnalyzer {

  private val log = Log(classOf[BulkNodeAnalyzerImpl])

  def analyze(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    val batchSize = 500
    Log.context("nodes") {
      val nodeCount = nodeIds.size
      log.info(s"Analyzing $nodeCount base nodes")
      log.infoElapsed {
        val nodeDocs = nodeIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.flatMap { case (batchNodeIds, batchIndex) =>
          val baseNodeDocs = nodeRepository.baseNodesWithIds(batchNodeIds)
          baseNodeDocs.flatMap { baseNodeDoc =>
            nodeMainAnalyzer.analyze(baseNodeDoc) match {
              case Some(nodeDoc) => Some(nodeDoc)
              case None =>
                log.error(s"Could not analyze node ${baseNodeDoc._id}")
                None
            }
          }
        }
        nodeRepository.bulkSave(nodeDocs: _*)
        (s"Analyzed $nodeCount nodes", nodeDocs)
      }
    }
  }
}
