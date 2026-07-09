package kpn.api.common.diff.network

import kpn.api.common.diff.TagDiffs

case class NetworkNodeDiff(
  connection: Option[Boolean] = None,
  roleConnection: Option[Boolean] = None,
  definedInNetworkRelation: Option[Boolean] = None,
  routeReferenceDiffs: Option[NodeRouteReferenceDiffs] = None,
  nodeIntegrityCheckDiff: Option[NodeIntegrityCheckDiff] = None,
  tagDiffs: Option[TagDiffs] = None
) {

  def nonEmpty: Boolean = {
    roleConnection.nonEmpty ||
      definedInNetworkRelation.nonEmpty ||
      routeReferenceDiffs.nonEmpty ||
      nodeIntegrityCheckDiff.nonEmpty ||
      tagDiffs.nonEmpty
  }

  def happy: Boolean = {
    false // TODO implement?
  }

  def investigate: Boolean = {
    false // TODO implement?
  }
}
