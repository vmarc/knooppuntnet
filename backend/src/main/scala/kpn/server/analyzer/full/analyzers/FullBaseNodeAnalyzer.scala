package kpn.server.analyzer.full.analyzers

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.base.BaseNodeMainAnalyzer
import kpn.server.repository.NodeRepository
import kpn.server.repository.RawDataRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class FullBaseNodeAnalyzer(
  rawDataRepository: RawDataRepository,
  nodeRepository: NodeRepository,
  baseNodeMainAnalyzer: BaseNodeMainAnalyzer,
) extends FullAnalyzer {

  private val log = Log(classOf[FullBaseNodeAnalyzer])
  private val NodeBatchSize = 500

  private case class AnalysisResult(
    analyzedIds: Seq[Long],
    obsoleteIds: Seq[Long]
  )

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("base-nodes") {
      log.infoElapsed {
        val result = analyzeNodes(context)
        (message(result), context)
      }
    }
  }

  private def analyzeNodes(context: FullAnalysisContext): AnalysisResult = {
    val activeNodeIds = nodeRepository.activeBaseNodeIds()
    val rawNodeIds = collectRawNodeIds(context.timestamp)
    val analyzedNodeIds = processNodesInBatches(context.timestamp, rawNodeIds)
    val obsoleteNodeIds = findObsoleteNodes(activeNodeIds, analyzedNodeIds)
    deactivateObsoleteNodes(obsoleteNodeIds)
    AnalysisResult(
      analyzedNodeIds,
      obsoleteNodeIds
    )
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

  private def processNodesInBatches(timestamp: Timestamp, rawNodeIds: Seq[Long]): Seq[Long] = {
    val nodeCount = rawNodeIds.size
    log.info(s"Analyzing $nodeCount base nodes")

    log.infoElapsed {
      val results = rawNodeIds
        .sliding(NodeBatchSize, NodeBatchSize)
        .toSeq
        .zipWithIndex
        .flatMap { case (batch, index) =>
          processBatch(timestamp, batch, index, nodeCount)
        }
      (s"Analyzed ${results.size} nodes", results)
    }
  }

  private def processBatch(
    timestamp: Timestamp,
    nodeIdsBatch: Seq[Long],
    batchIndex: Int,
    totalCount: Int
  ): Seq[Long] = {
    log.infoElapsed {
      val rawNodes = rawDataRepository.nodes(timestamp, nodeIdsBatch)
      val baseNodeDocs = rawNodes.flatMap { rawNode =>
        baseNodeMainAnalyzer.analyze(rawNode).orElse {
          log.error(s"Could not analyze node ${rawNode.id}")
          None
        }
      }

      nodeRepository.bulkSaveBaseNodes(baseNodeDocs)
      val processedIds = baseNodeDocs.map(_._id)
      (s"Analyzed ${NodeBatchSize * (batchIndex + 1)}/$totalCount nodes", processedIds)
    }
  }

  private def findObsoleteNodes(activeNodeIds: Seq[Long], analyzedNodeIds: Seq[Long]): Seq[Long] = {
    (activeNodeIds.toSet -- analyzedNodeIds).toSeq.sorted
  }

  private def deactivateObsoleteNodes(nodeIds: Seq[Long]): Unit = {
    nodeIds.flatMap(nodeRepository.baseNodeWithId).foreach(baseNodeDoc =>
      nodeRepository.saveBaseNode(baseNodeDoc.deactivated)
    )
  }

  private def message(result: AnalysisResult): String = {
    s"completed (${result.analyzedIds.size} nodes, ${result.obsoleteIds.size} obsolete nodes)"
  }
}
