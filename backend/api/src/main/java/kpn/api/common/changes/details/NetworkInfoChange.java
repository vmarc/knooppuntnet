package kpn.api.common.changes.details;

import kpn.api.common.ChangeType;
import kpn.api.common.Country;
import kpn.api.common.RouteType;
import kpn.api.common.common.ReferencedElements;
import kpn.api.common.diff.IdDiffs;
import kpn.api.common.diff.NetworkDataUpdate;
import kpn.api.common.diff.RefDiffs;
import kpn.core.doc.WithStringId;

import java.util.Optional;
import com.google.common.collect.ImmutableSet;

/*
  Describes the changes made to a given network in a given changeset.
*/
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
) implements WithStringId {
  public ReferencedElements referencedElements() {
    return new ReferencedElements(
      ImmutableSet.copyOf(nodeDiffs().ids()),
      ImmutableSet.copyOf(routeDiffs().ids())
    );
  }
}
