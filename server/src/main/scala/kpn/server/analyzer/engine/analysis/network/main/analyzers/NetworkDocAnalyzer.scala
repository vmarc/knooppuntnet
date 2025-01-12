package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.core.analysis.TagInterpreter
import kpn.core.doc.NodeDoc

class NetworkDocAnalyzer(context: NetworkAnalysisContext, nodeDoc: NodeDoc) {

  def roleConnection: Boolean = {
    context.network.members.find(_.ref == nodeDoc._id) match {
      case Some(nodeRef) => nodeRef.role.contains("connection")
      case None => false
    }
  }

  def longName: String = {
    val ln = nodeDoc.longName(context.scopedRouteType)
    if (ln.nonEmpty) ln else "-"
  }

  def expectedRouteCount: Option[Long] = {
    TagInterpreter.expectedRouteRelationCount(context.scopedRouteType, nodeDoc)
  }

  def connection: Boolean = {
    // the node is considered a connection node if all routes (in the network)
    // that contain this node have role "connection" in the network relation
    val nodeRouteDetails = context.routeDetails.filter(_.nodeRefs.contains(nodeDoc._id))
    val connectionRouteDetails = nodeRouteDetails.filter(_.role.contains("connection"))
    nodeRouteDetails.nonEmpty && connectionRouteDetails.sizeIs == nodeRouteDetails.sizeIs
  }

  def proposed: Boolean = {
    TagInterpreter.isProposedNode(context.scopedRouteType, nodeDoc)
  }

  def definedInRelation: Boolean = {
    context.network.members.exists(_.ref == nodeDoc._id)
  }
}
