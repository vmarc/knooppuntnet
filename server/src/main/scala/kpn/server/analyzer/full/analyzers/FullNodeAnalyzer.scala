package kpn.server.analyzer.full.analyzers

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzer
import kpn.server.repository.NodeRepository
import kpn.server.repository.RawDataRepository
import org.springframework.stereotype.Component

@Component
class FullNodeAnalyzer(
  rawDataRepository: RawDataRepository,
  nodeRepository: NodeRepository,
  bulkNodeAnalyzer: BulkNodeAnalyzer
) {

  private val log = Log(classOf[FullNodeAnalyzer])

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("full-node-analysis") {
      log.infoElapsed {
        val activeNodeIds = collectActiveNodeIds()
        val rawNodeIds = collectRawNodeIds(context.timestamp)
        val analyzedNodeIds = analyzeBaseNodes(context, rawNodeIds)
        val obsoleteNodeIds = (activeNodeIds.toSet -- analyzedNodeIds).toSeq.sorted
        deactivateObsoleteNodes(obsoleteNodeIds)
        (
          s"completed (${analyzedNodeIds.size} nodes, ${obsoleteNodeIds.size} obsolete nodes)",
          context.copy(
            obsoleteNodeIds = obsoleteNodeIds,
            nodeIds = analyzedNodeIds
          )
        )
      }
    }
  }

  private def collectActiveNodeIds(): Seq[Long] = {
    nodeRepository.activeNodeIds()
  }

  private def collectRawNodeIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting raw node ids")
    log.infoElapsed {
      val ids = rawDataRepository.nodeIds(timestamp)
      (s"Collected ${ids.size} raw node ids", ids)
    }
  }

  private def analyzeBaseNodes(context: FullAnalysisContext, rawNodeIds: Seq[Long]): Seq[Long] = {
    val batchSize = 500
    Log.context("base-nodes") {
      val nodeCount = rawNodeIds.size
      log.info(s"Analyzing $nodeCount base nodes")
      log.infoElapsed {
        val nodeDocs = bulkNodeAnalyzer.analyze(rawNodeIds)
        val ids = nodeDocs.map(_._id)
        (s"Analyzed ${nodeDocs.size} nodes", ids)
      }
    }
  }

  private def deactivateObsoleteNodes(nodeIds: Seq[Long]): Unit = {
    nodeIds.foreach { nodeId =>
      nodeRepository.nodeWithId(nodeId).map { nodeDoc =>
        nodeRepository.save(nodeDoc.deactivated)
      }
    }
  }
}
