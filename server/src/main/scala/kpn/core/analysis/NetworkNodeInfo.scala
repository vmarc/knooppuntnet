package kpn.core.analysis

import kpn.api.common.NodeIntegrityCheck
import kpn.api.common.route.RouteInfo
import kpn.api.custom.Day
import kpn.api.custom.Fact

case class NetworkNodeInfo(
  networkNode: NetworkNode,
  connection: Boolean,
  roleConnection: Boolean,
  definedInRelation: Boolean,
  definedInRoute: Boolean,
  proposed: Boolean,
  referencedInRoutes: Seq[RouteInfo],
  integrityCheck: Option[NodeIntegrityCheck],
  lastSurvey: Option[Day],
  facts: Seq[Fact]
) {

  def id: Long = networkNode.id

  def hasIntegrityCheck: Boolean = integrityCheck.isDefined

  def integrityCheckOk: Boolean = integrityCheck match {
    case Some(NodeIntegrityCheck(networkNodeName, networkNodeNodeId, actual, expected, failed)) => !failed
    case _ => false
  }
}
