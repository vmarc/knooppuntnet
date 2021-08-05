package kpn.server.analyzer.full.node

import kpn.core.mongo.Database
import kpn.core.mongo.doc.NodeDoc
import kpn.core.mongo.util.Id
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.analyzer.load.NodeLoader
import kpn.server.analyzer.load.orphan.node.NodeIdsLoader
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.ReplaceOneModel
import org.mongodb.scala.model.ReplaceOptions
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

@Component
class FullNodeAnalyzerImpl(
  database: Database,
  nodeIdsLoader: NodeIdsLoader,
  nodeLoader: NodeLoader,
  nodeAnalyzer: NodeAnalyzer,
  implicit val analysisExecutionContext: ExecutionContext
) extends FullNodeAnalyzer {

  private val log = Log(classOf[FullNodeAnalyzerImpl])

  override def analyze(context: FullAnalysisContext): FullAnalysisContext = {

    val existingNodeIds = findActiveNodeIds()

    val allNodeIds = log.infoElapsed {
      val ids = nodeIdsLoader.load(context.timestamp)
      ("load node ids", ids)
    }

    val batchSize = 500
    val updateFutures = allNodeIds.sliding(batchSize, batchSize).zipWithIndex.map { case (nodeIdsBatch, index) =>
      Future(
        Log.context(s"${index * batchSize}/${allNodeIds.size}") {
          val rawNodes = nodeLoader.load(context.timestamp, nodeIdsBatch)
          val nodeDocs = rawNodes.map { rawNode =>
            val nodeAnalysis = nodeAnalyzer.analyze(NodeAnalysis(rawNode))
            NodeDoc(
              nodeAnalysis.node.id,
              nodeAnalysis.labels,
              nodeAnalysis.active,
              nodeAnalysis.country,
              nodeAnalysis.name,
              nodeAnalysis.nodeNames,
              nodeAnalysis.node.latitude,
              nodeAnalysis.node.longitude,
              nodeAnalysis.node.timestamp,
              nodeAnalysis.lastSurvey,
              nodeAnalysis.node.tags,
              nodeAnalysis.facts,
              nodeAnalysis.locations,
              nodeAnalysis.tiles
            )
          }

          log.infoElapsed {
            val requests = nodeDocs.map { doc =>
              ReplaceOneModel[NodeDoc](Filters.equal("_id", doc._id), doc, ReplaceOptions().upsert(true))
            }
            val future = database.nodeDocs.native.bulkWrite(requests).toFuture()
            Await.result(future, Duration(2, TimeUnit.MINUTES))
            ("save", ())
          }
          rawNodes.map(_.id)
        }
      )
    }.toSeq

    val loadIdFuturesSeq = Future.sequence(updateFutures)

    val updateResult = Await.result(loadIdFuturesSeq, Duration(20, TimeUnit.MINUTES))
    val nodeIds = updateResult.flatten

    val obsoleteNodeIds = (existingNodeIds.toSet -- nodeIds).toSeq.sorted
    deactivateObsoleteNodes(obsoleteNodeIds)

    context.copy(
      obsoleteNodeIds = obsoleteNodeIds,
      nodeIds = nodeIds
    )
  }

  private def findActiveNodeIds(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("labels", "active")),
        project(
          fields(
            include("_id")
          )
        )
      )
      val nodeIds = database.nodes.aggregate[Id](pipeline, log).map(_._id)
      (s"{$nodeIds.size} existing nodes", nodeIds)
    }
  }

  private def deactivateObsoleteNodes(nodeIds: Seq[Long]): Unit = {
    nodeIds.foreach {
      nodeId =>
        database.nodes.findById(nodeId, log).map {
          nodeInfo =>
            // TODO log.warn(...)
            database.nodes.save(nodeInfo.copy(active = false), log)
        }
    }
  }
}
