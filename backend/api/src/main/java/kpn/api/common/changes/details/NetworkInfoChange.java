package kpn.api.common.changes.details;

import kpn.api.common.ChangeType;
import kpn.api.common.Country;
import kpn.api.common.RouteType;
import kpn.api.common.changes.details.ChangeKey;
import kpn.api.common.diff.IdDiffs;
import kpn.api.common.diff.NetworkDataUpdate;
import kpn.api.common.diff.RefDiffs;

import java.util.Optional;

public record NetworkInfoChange(
  String _id,
  ChangeKey key,
  ChangeType changeType,
  Optional<Country> country,
  RouteType routeType,
  Long networkId,
  String networkName,
  Optional<NetworkDataUpdate> networkDataUpdate,
  RefDiffs nodeDiffs,
  RefDiffs routeDiffs,
  IdDiffs extraNodeDiffs,
  IdDiffs extraWayDiffs,
  IdDiffs extraRelationDiffs,
  Boolean happy,
  Boolean investigate,
  Boolean impact
) {
}

/*
package kpn.api.common.changes.details

import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.common.common.ReferencedElements
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.WithStringId

*** 
  Describes the changes made to a given network in a given changeset.
 *** 
case class NetworkInfoChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  country: Option[Country],
  routeType: RouteType,
  networkId: Long,
  networkName: String,
  networkDataUpdate: Option[NetworkDataUpdate],
  nodeDiffs: RefDiffs,
  routeDiffs: RefDiffs,
  extraNodeDiffs: IdDiffs,
  extraWayDiffs: IdDiffs,
  extraRelationDiffs: IdDiffs,
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean
) extends WithStringId {

  def referencedElements: ReferencedElements = {
    ReferencedElements(nodeDiffs.ids.toSet, routeDiffs.ids.toSet)
  }
}

*/
