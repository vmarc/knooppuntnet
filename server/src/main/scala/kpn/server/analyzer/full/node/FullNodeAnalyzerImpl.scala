package kpn.server.analyzer.full.node

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.node.base.BaseNodeMainAnalyzer
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.repository.NodeRepository
import kpn.server.repository.RawDataRepository
import org.springframework.stereotype.Component

@Component
class FullNodeAnalyzerImpl(
  database: Database,
  rawDataRepository: RawDataRepository,
  nodeRepository: NodeRepository,
  baseNodeMainAnalyzer: BaseNodeMainAnalyzer,
) extends FullNodeAnalyzer {

  private val log = Log(classOf[FullNodeAnalyzerImpl])

  override def analyze(context: FullAnalysisContext): FullAnalysisContext = {
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
        rawNodeIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.foreach { case (nodeIdsBatch, index) =>
          log.infoElapsed {
            val rawNodes = rawDataRepository.nodes(context.timestamp, nodeIdsBatch)
            rawNodes.foreach { rawNode =>
              baseNodeMainAnalyzer.analyze(rawNode) match {
                case None => log.error(s"Could not analyze node ${rawNode.id}")
                case Some(baseNodeDoc) =>
                  nodeRepository.saveBaseNode(baseNodeDoc)
              }
            }
            (s"Analyzed ${batchSize * (index + 1)}/$nodeCount nodes", ())
          }
        }
        (s"Analyzed $nodeCount nodes", ())
      }
    }
    rawNodeIds // TODO collect ids in logic above
    //    val nodeCount = rawNodeIds.size
    //    val updateFutures = rawNodeIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.map { case (nodeIdsBatch, index) =>
    //      Future(
    //        Log.context(s"${index * batchSize}/$nodeCount") {
    //          log.infoElapsed {
    //            val baseNodeDocs = rawDataRepository.nodes(context.timestamp, nodeIdsBatch).flatMap(baseNodeMainAnalyzer.analyze)
    //            nodeRepository.bulkSaveBaseNodes(baseNodeDocs)
    //            val ids = baseNodeDocs.map(_._id)
    //            (s"analyzed ${ids.size} base nodes: ${ids.mkString(", ")}", ids)
    //          }
    //        }
    //      )
    //    }
    //
    //    val loadIdFuturesSeq = Future.sequence(updateFutures)
    //    val updateResult = Await.result(loadIdFuturesSeq, Duration(3, TimeUnit.HOURS))
    //    updateResult.flatten
  }

  private def deactivateObsoleteNodes(nodeIds: Seq[Long]): Unit = {
    nodeIds.foreach { nodeId =>
      database.nodes.findById(nodeId, log).map { nodeDoc =>
        database.nodes.save(nodeDoc.deactivated, log)
      }
    }
  }
}
