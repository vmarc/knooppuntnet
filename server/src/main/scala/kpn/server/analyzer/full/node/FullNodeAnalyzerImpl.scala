package kpn.server.analyzer.full.node

import kpn.api.custom.Timestamp
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NodeDoc
import kpn.core.mongo.util.Id
import kpn.core.mongo.util.Mongo
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeLocationsAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeRouteReferencesAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeTileAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.analyzer.load.NodeLoader
import kpn.server.analyzer.load.NodeLoaderImpl
import kpn.server.analyzer.load.orphan.node.NodeIdsLoader
import kpn.server.analyzer.load.orphan.node.NodeIdsLoaderImpl
import kpn.server.repository.NodeRepositoryImpl
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.ReplaceOneModel
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

object FullNodeAnalyzerImpl {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      val executionContext = ExecutionContext.fromExecutor(
        // TODO use ServerConfiguration.analysisExecutor with given pool size and name
        Executors.newFixedThreadPool(9)
      )
      val executor = new OverpassQueryExecutorRemoteImpl()
      val nodeIdsLoader = new NodeIdsLoaderImpl(executor)
      val nodeLoader = new NodeLoaderImpl(null, executor, null, null)
      val analysisContext = new AnalysisContext()
      val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
      val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
      val nodeCountryAnalyzer = new NodeCountryAnalyzerImpl(countryAnalyzer)
      val tileCalculator = new TileCalculatorImpl()
      val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)
      val nodeTileAnalyzer = new NodeTileAnalyzerImpl(nodeTileCalculator)
      val locationConfiguration = new LocationConfigurationReader().read()
      val nodeLocationsAnalyzer = new NodeLocationsAnalyzerImpl(
        locationConfiguration,
        analyzerEnabled = true
      )
      val nodeRepository = new NodeRepositoryImpl(database, null, true)
      val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzerImpl(nodeRepository) // TODO this should not be here???
      val nodeAnalyzer = new NodeAnalyzerImpl(
        nodeCountryAnalyzer,
        nodeTileAnalyzer,
        nodeLocationsAnalyzer,
        nodeRouteReferencesAnalyzer
      )
      val analyzer = new FullNodeAnalyzerImpl(
        database,
        nodeIdsLoader,
        nodeLoader,
        nodeAnalyzer,
        executionContext
      )
      val context = FullAnalysisContext(Timestamp(2021, 8, 2, 0, 0, 0))
      val contextAfter = analyzer.analyze(context)
      println("nodeIds: " + contextAfter.nodeIds.size)
      println("obsolete nodeIds: " + contextAfter.obsoleteNodeIds)
    }
  }
}

class FullNodeAnalyzerImpl(
  database: Database,
  nodeIdsLoader: NodeIdsLoader,
  nodeLoader: NodeLoader,
  nodeAnalyzer: NodeAnalyzer,
  implicit val executionContext: ExecutionContext
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
