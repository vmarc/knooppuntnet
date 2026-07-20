package kpn.api.common.diff.network;

import kpn.api.common.diff.TagDiffs;
import kpn.api.common.diff.network.NodeIntegrityCheckDiff;
import kpn.api.common.diff.network.NodeRouteReferenceDiffs;

import java.util.Optional;

public record NetworkNodeDiff(
  Optional<Boolean> connection,
  Optional<Boolean> roleConnection,
  Optional<Boolean> definedInNetworkRelation,
  Optional<NodeRouteReferenceDiffs> routeReferenceDiffs,
  Optional<NodeIntegrityCheckDiff> nodeIntegrityCheckDiff,
  Optional<TagDiffs> tagDiffs
) {
}

/* TODO migrate
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

*/
