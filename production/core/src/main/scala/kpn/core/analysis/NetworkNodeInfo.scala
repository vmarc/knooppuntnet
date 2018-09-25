package kpn.core.analysis

import kpn.shared.NodeIntegrityCheck
import kpn.shared.route.RouteInfo

case class NetworkNodeInfo(
  networkNode: NetworkNode,
  roleConnection: Boolean,
  definedInRelation: Boolean,
  definedInRoute: Boolean,
  referencedInRoutes: Seq[RouteInfo],
  integrityCheck: Option[NodeIntegrityCheck]
) {

  def id: Long = networkNode.id

  def hasIntegrityCheck: Boolean = integrityCheck.isDefined

  def integrityCheckOk: Boolean = integrityCheck match {
    case Some(NodeIntegrityCheck(networkNodeName, networkNodeNodeId, actual, expected, failed)) => !failed
    case _ => false
  }
}
