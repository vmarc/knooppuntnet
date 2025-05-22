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
  bulkNodeAnalyzer: BulkNodeAnalyzer,
  initialNodeChangeBuilder: InitialNodeChangeBuilder
) extends FullAnalyzer {

  private val log = Log(classOf[FullNodeAnalyzer])

  case class NodeAnalysisResult(
    analyzedIds: Seq[Long],
    obsoleteIds: Seq[Long]
  )

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("full-node-analysis") {
      log.infoElapsed {
        val result = analyzeNodes(context)
        updateContext(context, result)
      }
    }
  }

  private def analyzeNodes(context: FullAnalysisContext): NodeAnalysisResult = {
    val activeNodeIds = findActiveNodeIds()
    val baseNodeIds = findBaseNodeIds()
    val analyzedNodeIds = processBaseNodes(context, baseNodeIds)
    val obsoleteNodeIds = (activeNodeIds.toSet -- analyzedNodeIds).toSeq.sorted

    deactivateObsoleteNodesBatch(obsoleteNodeIds)
    NodeAnalysisResult(analyzedNodeIds, obsoleteNodeIds)
  }

  private def findActiveNodeIds(): Seq[Long] = {
    nodeRepository.activeNodeIds()
  }

  private def findBaseNodeIds(): Seq[Long] = {
    log.info("Collecting node ids from base nodes")
    log.infoElapsed {
      val ids = nodeRepository.activeBaseNodeIds()
      (s"Collected ${ids.size} node ids", ids)
    }
  }

  private def processBaseNodes(context: FullAnalysisContext, baseNodeIds: Seq[Long]): Seq[Long] = {
    Log.context("base-nodes") {
      log.info(s"Analyzing ${baseNodeIds.size} base nodes")
      log.infoElapsed {
        val nodeDocs = bulkNodeAnalyzer.analyze(baseNodeIds)
        context.initialAnalysisChangeSetContext.foreach { changeSetContext =>
          nodeDocs.foreach { nodeDoc =>
            initialNodeChangeBuilder.buildAndSave(changeSetContext, nodeDoc)
          }
        }
        val processedIds = nodeDocs.map(_._id)
        (s"Analyzed ${processedIds.size} nodes", processedIds)
      }
    }
  }

  private def deactivateObsoleteNodesBatch(nodeIds: Seq[Long]): Unit = {
    nodeIds.grouped(1000).foreach { batch =>
      val nodeDocs = batch.flatMap(nodeRepository.nodeWithId)
      nodeDocs.foreach(doc => nodeRepository.save(doc.deactivated))
    }
  }

  private def updateContext(context: FullAnalysisContext, result: NodeAnalysisResult): (String, FullAnalysisContext) = {
    val message = s"completed (${result.analyzedIds.size} nodes, ${result.obsoleteIds.size} obsolete nodes)"
    val updatedContext = context.copy(
      obsoleteNodeIds = result.obsoleteIds,
      nodeIds = result.analyzedIds
    )
    (message, updatedContext)
  }
}
