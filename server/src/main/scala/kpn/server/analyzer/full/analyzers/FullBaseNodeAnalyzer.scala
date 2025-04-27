package kpn.server.analyzer.full.analyzers

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.base.BaseNodeMainAnalyzer
import kpn.server.repository.NodeRepository
import kpn.server.repository.RawDataRepository
import org.springframework.stereotype.Component

@Component
class FullBaseNodeAnalyzer(
  rawDataRepository: RawDataRepository,
  nodeRepository: NodeRepository,
  baseNodeMainAnalyzer: BaseNodeMainAnalyzer,
) extends FullAnalyzer {

  private val log = Log(classOf[FullBaseNodeAnalyzer])

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("base-nodes") {
      log.infoElapsed {
        val activeNodeIds = collectActiveBaseNodeIds()
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

  private def collectActiveBaseNodeIds(): Seq[Long] = {
    nodeRepository.activeBaseNodeIds()
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
        val ids = rawNodeIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.flatMap { case (nodeIdsBatch, index) =>
          log.infoElapsed {
            val rawNodes = rawDataRepository.nodes(context.timestamp, nodeIdsBatch)
            val baseNodeDocs = rawNodes.flatMap { rawNode =>
              baseNodeMainAnalyzer.analyze(rawNode) match {
                case Some(baseNodeDoc) => Some(baseNodeDoc)
                case None =>
                  log.error(s"Could not analyze node ${rawNode.id}")
                  None
              }
            }
            nodeRepository.bulkSaveBaseNodes(baseNodeDocs)
            val baseNodeIds = baseNodeDocs.map(_._id)
            (s"Analyzed ${batchSize * (index + 1)}/$nodeCount nodes", baseNodeIds)
          }
        }
        (s"Analyzed ${ids.size} nodes", ids)
      }
    }
  }

  private def deactivateObsoleteNodes(nodeIds: Seq[Long]): Unit = {
    nodeIds.foreach { nodeId =>
      nodeRepository.baseNodeWithId(nodeId).map { baseNodeDoc =>
        nodeRepository.saveBaseNode(baseNodeDoc.deactivated)
      }
    }
  }
}
