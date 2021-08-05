package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.common.NodeRouteRefs
import kpn.api.common.common.Reference
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.custom.NetworkScope
import kpn.api.custom.ScopedNetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNodeRouteReferences
import kpn.core.mongo.doc.NetworkDoc
import kpn.core.mongo.doc.NodeDoc
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
import kpn.server.analyzer.engine.analysis.network.info.NetworkInfoMasterAnalyzer
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
    val nodeDetails = buildNetworkNodes(context.networkDoc, context.scopedNetworkType, nodeDocs)

    context.copy(
      nodeDetails = nodeDetails
    )
  }

  private def buildNetworkNodes(networkDoc: NetworkDoc, scopedNetworkType: ScopedNetworkType, nodeDocs: Seq[NodeDoc]): Seq[NetworkNodeDetail] = {
    val sortedNodeDocs = NaturalSorting.sortBy(nodeDocs)(_.name(scopedNetworkType))
    val routeReferences = nodesRouteReferences(scopedNetworkType, sortedNodeDocs.map(_._id))
    val nodeRouteReferencesMap = routeReferences.map(nrr => nrr.nodeId -> nrr.routeRefs).toMap
    sortedNodeDocs.map { nodeDoc =>
      val roleConnection: Boolean = networkDoc.nodeMembers.find(_.nodeId == nodeDoc._id) match {
        case Some(nodeRef) => nodeRef.role.contains("connection")
        case None => false
      }
      val ln = nodeDoc.longName(scopedNetworkType)
      val longName = if (ln.nonEmpty) ln else "-"
      NetworkNodeDetail(
        nodeDoc._id,
        nodeDoc.name(scopedNetworkType),
        longName,
        nodeDoc.latitude,
        nodeDoc.longitude,
        connection = false, // not supported anymore
        roleConnection = roleConnection,
        definedInRelation = networkDoc.nodeMembers.exists(_.nodeId == nodeDoc._id),
        definedInRoute = false, // not supported anymore
        proposed = nodeDoc.labels.contains("proposed"), // networkInfoNode.proposed,
        nodeDoc.lastUpdated,
        nodeDoc.lastSurvey,
        "-", //networkInfoNode.integrityCheck.map(_.expected.toString).getOrElse("-"),
        nodeRouteReferencesMap.getOrElse(nodeDoc._id, Seq.empty),
        nodeDoc.facts,
        nodeDoc.tags // TODO MONGO should not be needed?
      )
    }
  }

  private def queryNodes(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", "active"),
            in("_id", nodeIds: _*)
          ),
        )
      )
      val nodes = database.nodeDocs.aggregate[NodeDoc](pipeline, log)
      (s"nodes: ${nodes.size}", nodes)
    }
  }

  private def nodesRouteReferences(scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[NodeRouteRefs] = {
    val nodeRouteRefs = new MongoQueryNodeRouteReferences(database).execute(nodeIds)
    nodeIds.map { nodeId =>
      val references = nodeRouteRefs.filter(_.nodeId == nodeId).map { nodeRouteRef =>
        Reference(
          nodeRouteRef.networkType,
          NetworkScope.regional, // TODO MONGO pick up the real NetworkScope from the route collection
          nodeRouteRef.routeId,
          nodeRouteRef.routeName
        )
      }.sortBy(_.name)
      NodeRouteRefs(nodeId, references)
    }
  }
}
