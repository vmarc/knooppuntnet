package kpn.server.analyzer.full.analyzers

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
) extends FullAnalyzer {

  private val log = Log(classOf[FullNodeAnalyzer])

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("full-node-analysis") {
      log.infoElapsed {
        val activeNodeIds = collectActiveNodeIds()
        val rawNodeIds = collectBaseNodeIds()
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

  private def collectBaseNodeIds(): Seq[Long] = {
    log.info("Collecting base node ids")
    log.infoElapsed {
      val ids = nodeRepository.activeBaseNodeIds()
      (s"Collected ${ids.size} raw node ids", ids)
    }
  }

  private def analyzeBaseNodes(context: FullAnalysisContext, rawNodeIds: Seq[Long]): Seq[Long] = {
    Log.context("base-nodes") {
      log.info(s"Analyzing ${rawNodeIds.size} base nodes")
      log.infoElapsed {
        val nodeDocs = bulkNodeAnalyzer.analyze(rawNodeIds)
        val ids = nodeDocs.map(_._id)
        (s"Analyzed ${ids.size} nodes", ids)
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
