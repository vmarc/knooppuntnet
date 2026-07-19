package kpn.api.common.changes.details;

import kpn.api.common.ChangeType;
import kpn.api.common.Country;
import kpn.api.common.RouteType;
import kpn.api.common.changes.details.ChangeKey;
import kpn.api.common.data.MetaData;
import kpn.api.common.diff.IdDiffs;
import kpn.api.common.diff.RefDiffs;

import java.util.Optional;

public record NetworkChangeInfo(
  Long rowIndex,
  Optional<String> comment,
  ChangeKey key,
  ChangeType changeType,
  Optional<Country> country,
  RouteType routeType,
  Long networkId,
  Optional<String> networkName,
  Optional<MetaData> before,
  Optional<MetaData> after,
  Boolean networkDataUpdated,
  RefDiffs networkNodes,
  RefDiffs routes,
  IdDiffs nodes,
  IdDiffs ways,
  IdDiffs relations,
  Boolean happy,
  Boolean investigate
) {
}

/*
package kpn.api.common.changes.details

import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.common.common.ReferencedElements
import kpn.api.common.data.MetaData
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs

case class NetworkChangeInfo(
  rowIndex: Long,
  comment: Option[String],
  key: ChangeKey,
  changeType: ChangeType,
  country: Option[Country],
  routeType: RouteType,
  networkId: Long,
  networkName: Option[String],
  before: Option[MetaData],
  after: Option[MetaData],
  networkDataUpdated: Boolean,
  networkNodes: RefDiffs,
  routes: RefDiffs,
  nodes: IdDiffs,
  ways: IdDiffs,
  relations: IdDiffs,
  happy: Boolean,
  investigate: Boolean
) {

  def referencedElements: ReferencedElements = {
    ReferencedElements(nodes.ids.toSet, routes.ids.toSet)
  }
}

*/
