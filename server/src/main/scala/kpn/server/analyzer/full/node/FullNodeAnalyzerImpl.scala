package kpn.server.analyzer.full.node

import kpn.api.custom.Timestamp
import kpn.database.base.Database
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzer
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.overpass.OverpassRepository
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

@Component
class FullNodeAnalyzerImpl(
  database: Database,
  overpassRepository: OverpassRepository,
  nodeRepository: NodeRepository,
  bulkNodeAnalyzer: BulkNodeAnalyzer,
  implicit val analysisExecutionContext: ExecutionContext
) extends FullNodeAnalyzer {

  private val log = Log(classOf[FullNodeAnalyzerImpl])

  override def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("full-node-analysis") {
      log.infoElapsed {
        val activeNodeIds = collectActiveNodeIds()
        val overpassNodeIds = collectOverpassNodeIds(context.timestamp)
        val analyzedNodeIds = analyzeNodes(context, overpassNodeIds)
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

  private def collectOverpassNodeIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting overpass node ids")
    log.infoElapsed {
      val ids = overpassRepository.nodeIds(timestamp)
      (s"Collected ${ids.size} overpass node ids", ids)
    }
  }

  private def analyzeNodes(context: FullAnalysisContext, overpassNodeIds: Seq[Long]): Seq[Long] = {
    val batchSize = 500
    val updateFutures = overpassNodeIds.sliding(batchSize, batchSize).zipWithIndex.map { case (nodeIdsBatch, index) =>
      Future(
        Log.context(s"${index * batchSize}/${overpassNodeIds.size}") {
          log.infoElapsed {
            val ids = bulkNodeAnalyzer.analyze(context.timestamp, nodeIdsBatch).map(_._id)
            (s"analyzed ${ids.size} nodes: ${ids.mkString(", ")}", ids)
          }
        }
      )
    }.toSeq

    val loadIdFuturesSeq = Future.sequence(updateFutures)
    val updateResult = Await.result(loadIdFuturesSeq, Duration(3, TimeUnit.HOURS))
    updateResult.flatten
  }

  private def deactivateObsoleteNodes(nodeIds: Seq[Long]): Unit = {
    nodeIds.foreach { nodeId =>
      database.nodes.findById(nodeId, log).map { nodeDoc =>
        database.nodes.save(nodeDoc.deactivated, log)
      }
    }
  }
}
