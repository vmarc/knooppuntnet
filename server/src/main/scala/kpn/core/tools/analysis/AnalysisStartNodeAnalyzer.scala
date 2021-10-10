package kpn.core.tools.analysis

import kpn.api.common.LatLonImpl
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.diff.common.FactDiffs
import kpn.api.custom.ChangeType
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.doc.NodeDoc
import kpn.core.util.Log

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class AnalysisStartNodeAnalyzer(log: Log, config: AnalysisStartConfiguration)(implicit val executionContext: ExecutionContext) {

  def analyze(): Unit = {
    Log.context("node-analysis") {
      log.infoElapsed {
        val overpassNodeIds = collectOverpassNodeIds(config.timestamp)
        val databaseNodeIds = config.nodeRepository.allNodeIds()
        val nodeIds = (overpassNodeIds.toSet -- databaseNodeIds.toSet).toSeq.sorted
        val analyzedNodeIds = analyzeNodes(nodeIds)
        (s"completed (${analyzedNodeIds.size} nodes", ())
      }
    }
  }

  private def collectOverpassNodeIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting overpass node ids")
    log.infoElapsed {
      val ids = config.overpassRepository.nodeIds(timestamp)
      (s"Collected ${ids.size} overpass node ids", ids)
    }
  }

  private def analyzeNodes(overpassNodeIds: Seq[Long]): Seq[Long] = {
    val batchSize = 500
    val updateFutures = overpassNodeIds.sliding(batchSize, batchSize).zipWithIndex.map { case (nodeIdsBatch, index) =>
      Future(
        Log.context(s"${index * batchSize}/${overpassNodeIds.size}") {
          log.infoElapsed {
            val nodeDocs = config.bulkNodeAnalyzer.analyze(config.timestamp, nodeIdsBatch)
            nodeDocs.foreach { nodeDoc =>
              loadNodeChange(nodeDoc)
            }
            val ids = nodeDocs.map(_._id)
            (s"analyzed ${ids.size} nodes: ${ids.mkString(", ")}", ids)
          }
        }
      )
    }.toSeq

    val loadIdFuturesSeq = Future.sequence(updateFutures)
    val updateResult = Await.result(loadIdFuturesSeq, Duration(2, TimeUnit.HOURS))
    updateResult.flatten
  }

  private def loadNodeChange(nodeDoc: NodeDoc): Unit = {

    def subsets: Seq[Subset] = {
      nodeDoc.country match {
        case None => Seq.empty
        case Some(c) =>
          val networkTypes = nodeDoc.names.map(_.networkType).distinct
          networkTypes.map(n => Subset(c, n))
      }
    }

    val key = config.changeSetContext.buildChangeKey(nodeDoc._id)
    val facts = nodeDoc.facts.toSet
    val locationFacts = facts.filter(Fact.locationFacts.contains)

    config.changeSetRepository.saveNodeChange(
      NodeChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.InitialValue,
        subsets = subsets,
        locations = nodeDoc.locations,
        name = nodeDoc.name,
        before = None,
        after = Some(nodeDoc.toMeta),
        connectionChanges = Seq.empty,
        roleConnectionChanges = Seq.empty,
        definedInNetworkChanges = Seq.empty,
        tagDiffs = None,
        nodeMoved = None,
        addedToRoute = Seq.empty,
        removedFromRoute = Seq.empty,
        addedToNetwork = Seq.empty,
        removedFromNetwork = Seq.empty,
        factDiffs = FactDiffs(remaining = facts),
        facts = Seq.empty,
        initialTags = Some(nodeDoc.tags),
        initialLatLon = Some(LatLonImpl(nodeDoc.latitude, nodeDoc.longitude)),
        tiles = nodeDoc.tiles,
        investigate = facts.nonEmpty,
        impact = true,
        locationInvestigate = locationFacts.nonEmpty,
        locationImpact = true
      )
    )
  }
}
