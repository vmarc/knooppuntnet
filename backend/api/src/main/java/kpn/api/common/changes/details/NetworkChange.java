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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public record NetworkChange(
  String _id,
  ChangeKey key,
  Long networkId,
  Optional<String> networkName,
  ChangeType changeType,
  Optional<Country> country,
  RouteType routeType,
  Optional<NetworkDataUpdate> networkDataUpdate,
  IdDiffs nodes,
  IdDiffs ways,
  IdDiffs relations,
  RefDiffs nodeDiffs,
  RefDiffs routeDiffs,
  IdDiffs extraNodeDiffs,
  IdDiffs extraWayDiffs,
  IdDiffs extraRelationDiffs,
  Boolean happy,
  Boolean investigate,
  Boolean impact
) implements WithStringId {

  public ImmutableList<Long> impactedNodeIds() {
    return nodeDiffs().ids();
  }

  public ImmutableList<Long> impactedRelationIds() {
    return relations().ids();
  }

  public ImmutableList<Long> impactedElementIds() {
    return ImmutableList.<Long>builder()
      .addAll(impactedNodeIds())
      .addAll(impactedRelationIds())
      .build();
  }

  public ReferencedElements referencedElements() {
    return new ReferencedElements(
      ImmutableSet.copyOf(nodeDiffs().ids()),
      ImmutableSet.copyOf(routeDiffs().ids())
    );
  }
}
