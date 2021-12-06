package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.network.Integrity
import kpn.core.doc.Label
import kpn.core.doc.NetworkInfoNodeDetail
import kpn.core.doc.NodeDoc
import kpn.core.util.Formatter.percentage
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.springframework.stereotype.Component

@Component
class NetworkInfoNodeAnalyzer(database: Database) extends NetworkInfoAnalyzer {

  private val log = Log(classOf[NetworkInfoNodeAnalyzer])

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    val routeNodeIds = context.routeDetails.flatMap(_.nodeRefs).distinct.sorted
    val networkNodeIds = context.networkDoc.nodeMembers.map(_.nodeId)
    val nodeIds = (networkNodeIds ++ routeNodeIds).distinct.sorted
    val nodeDocs = queryNodes(nodeIds)
    val nodeDetails = analyzeNetworkNodes(context, nodeDocs)
    val integrity = analyzeIntegrity(context, nodeDocs)
    context.copy(
      nodeDetails = nodeDetails,
      integrity = integrity
    )
  }

  private def analyzeNetworkNodes(context: NetworkInfoAnalysisContext, nodeDocs: Seq[NodeDoc]): Seq[NetworkInfoNodeDetail] = {
    val sortedNodeDocs = NaturalSorting.sortBy(nodeDocs)(_.name(context.scopedNetworkType))
    sortedNodeDocs.map { nodeDoc =>
      val networkDocAnalyzer = new NetworkDocAnalyzer(context, nodeDoc)
      NetworkInfoNodeDetail(
        nodeDoc._id,
        nodeDoc.name(context.scopedNetworkType),
        networkDocAnalyzer.longName,
        nodeDoc.latitude,
        nodeDoc.longitude,
        networkDocAnalyzer.connection,
        networkDocAnalyzer.roleConnection,
        networkDocAnalyzer.definedInRelation,
        networkDocAnalyzer.proposed,
        nodeDoc.lastUpdated,
        nodeDoc.lastSurvey,
        networkDocAnalyzer.expectedRouteCount,
        nodeDoc.facts
      )
    }
  }

  private def analyzeIntegrity(context: NetworkInfoAnalysisContext, nodeDocs: Seq[NodeDoc]): Integrity = {
    val networkNodeIntegrities = nodeDocs.flatMap { nodeDoc =>
      nodeDoc.integrity.toSeq.flatMap { integrity =>
        integrity.details.filter { nodeIntegrityDetail =>
          nodeIntegrityDetail.networkType == context.scopedNetworkType.networkType &&
            nodeIntegrityDetail.networkScope == context.scopedNetworkType.networkScope
        }.map { nodeIntegrityDetail =>
          NetworkNodeIntegrity(
            nodeDoc._id,
            nodeIntegrityDetail.expectedRouteCount,
            nodeIntegrityDetail.routeRefs.size
          )
        }
      }
    }

    val isOk = if (networkNodeIntegrities.isEmpty) {
      true
    }
    else {
      networkNodeIntegrities.forall(_.ok)
    }
    val hasChecks = networkNodeIntegrities.nonEmpty
    val count = if (networkNodeIntegrities.isEmpty) "-" else networkNodeIntegrities.size.toString
    val okCount = networkNodeIntegrities.count(_.ok)
    val nokCount = networkNodeIntegrities.count(_.notOk)
    val coverage = percentage(networkNodeIntegrities.size, nodeDocs.size)
    val okRate = percentage(okCount, networkNodeIntegrities.size)
    val nokRate = percentage(nokCount, networkNodeIntegrities.size)

    Integrity(
      isOk,
      hasChecks,
      count,
      okCount,
      nokCount,
      coverage,
      okRate,
      nokRate
    )
  }

  private def queryNodes(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            in("_id", nodeIds: _*)
          ),
        )
      )
      val nodes = database.nodes.aggregate[NodeDoc](pipeline, log)
      (s"nodes: ${nodes.size}", nodes)
    }
  }
}
