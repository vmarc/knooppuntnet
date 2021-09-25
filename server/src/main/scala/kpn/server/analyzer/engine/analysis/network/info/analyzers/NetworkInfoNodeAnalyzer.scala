package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.network.NetworkNodeDetail
import kpn.core.mongo.Database
import kpn.core.mongo.doc.Label
import kpn.core.mongo.doc.NodeDoc
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
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

    context.copy(
      nodeDetails = nodeDetails
    )
  }

  private def analyzeNetworkNodes(context: NetworkInfoAnalysisContext, nodeDocs: Seq[NodeDoc]): Seq[NetworkNodeDetail] = {
    val sortedNodeDocs = NaturalSorting.sortBy(nodeDocs)(_.name(context.scopedNetworkType))
    sortedNodeDocs.map { nodeDoc =>
      val networkDocAnalyzer = new NetworkDocAnalyzer(context, nodeDoc)
      NetworkNodeDetail(
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