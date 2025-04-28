package kpn.core.tools.analysis

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzer
import kpn.server.overpass.OverpassRepository
import kpn.server.repository.NodeRepository

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class AnalysisStartBaseNodeAnalyzer(
  nodeRepository: NodeRepository,
  overpassRepository: OverpassRepository,
  bulkNodeAnalyzer: BulkNodeAnalyzer,
  nodeChangeBuilder: AnalysisStartNodeChangeBuilder
)(implicit val executionContext: ExecutionContext) {
  private val BatchSize = 500
  private val MaxWaitDuration = Duration(2, "hours")
  private val log = Log(classOf[AnalysisStartBaseNodeAnalyzer])

  def analyze(context: AnalysisStartContext): AnalysisStartContext = {
    Log.context("node-analysis") {
      log.infoElapsed {
        val nodeIdsToAnalyze = determineNodesToAnalyze(context.timestamp)
        val analyzedNodeIds = processNodesInBatches(nodeIdsToAnalyze)
        (s"completed ${analyzedNodeIds.size} nodes", ())
      }
      context
    }
  }

  private def determineNodesToAnalyze(timestamp: Timestamp): Seq[Long] = {
    val databaseNodeIds = collectActiveBaseNodeIds()
    val overpassNodeIds = collectOverpassNodeIds(timestamp)
    (overpassNodeIds.toSet -- databaseNodeIds.toSet).toSeq.sorted
  }

  private def collectActiveBaseNodeIds(): Seq[Long] = {
    nodeRepository.activeBaseNodeIds()
  }

  private def collectOverpassNodeIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting overpass node ids")
    log.infoElapsed {
      val ids = overpassRepository.nodeIds(timestamp)
      (s"Collected ${ids.size} overpass node ids", ids)
    }
  }

  private def processNodesInBatches(nodeIds: Seq[Long]): Seq[Long] = {
    val totalNodes = nodeIds.size
    val batchFutures = createBatchProcessingFutures(nodeIds, totalNodes)
    val loadIdFuturesSeq = Future.sequence(batchFutures)
    Await.result(loadIdFuturesSeq, MaxWaitDuration).flatten
  }

  private def createBatchProcessingFutures(nodeIds: Seq[Long], totalNodes: Int): Seq[Future[Seq[Long]]] = {
    nodeIds.sliding(BatchSize, BatchSize).zipWithIndex.map { case (nodeIdsBatch, index) =>
      Future(processBatch(nodeIdsBatch, index * BatchSize, totalNodes))
    }.toSeq
  }

  private def processBatch(nodeIdsBatch: Seq[Long], offset: Int, total: Int): Seq[Long] = {
    Log.context(s"$offset/$total") {
      log.infoElapsed {
        val nodeDocs = bulkNodeAnalyzer.analyze(nodeIdsBatch)
        nodeDocs.foreach(doc => nodeChangeBuilder.buildAndSave(doc))
        val ids = nodeDocs.map(_._id)
        (s"analyzed ${ids.size} nodes: ${ids.mkString(", ")}", ids)
      }
    }
  }
}
