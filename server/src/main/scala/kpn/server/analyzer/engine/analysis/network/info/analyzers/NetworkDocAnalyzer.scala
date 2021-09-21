package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.core.analysis.TagInterpreter
import kpn.core.mongo.doc.NodeDoc
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

class NetworkDocAnalyzer(context: NetworkInfoAnalysisContext, nodeDoc: NodeDoc) {

  def roleConnection: Boolean = {
    context.networkDoc.nodeMembers.find(_.nodeId == nodeDoc._id) match {
      case Some(nodeRef) => nodeRef.role.contains("connection")
      case None => false
    }
  }

  def longName: String = {
    val ln = nodeDoc.longName(context.scopedNetworkType)
    if (ln.nonEmpty) ln else "-"
  }

  def expectedRouteCount: Option[Long] = {
    TagInterpreter.expectedRouteRelationCount(context.scopedNetworkType, nodeDoc.tags)
  }

  def connection: Boolean = {
    // the node is considered a connection node if all routes (in the network)
    // that contain this node have role "connection" in the network relation
    val nodeRouteDetails = context.routeDetails.filter(_.nodeRefs.contains(nodeDoc._id))
    val connectionRouteDetails = nodeRouteDetails.filter(_.role.contains("connection"))
    nodeRouteDetails.nonEmpty && connectionRouteDetails.size == nodeRouteDetails.size
  }

  def proposed: Boolean = {
    TagInterpreter.isProposedNode(context.scopedNetworkType, nodeDoc.tags)
  }

  def definedInRelation: Boolean = {
    context.networkDoc.nodeMembers.exists(_.nodeId == nodeDoc._id)
  }
}
