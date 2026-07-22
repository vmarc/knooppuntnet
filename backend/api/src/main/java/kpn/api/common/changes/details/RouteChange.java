package kpn.api.common.changes.details;

import kpn.api.common.ChangeType;
import kpn.api.common.Fact;
import kpn.api.common.RouteLocationAnalysis;
import kpn.api.common.common.Ref;
import kpn.api.common.common.ReferencedElements;
import kpn.api.common.diff.RouteData;
import kpn.api.common.route.RouteNode;
import kpn.api.common.route.RouteNodeChange;
import kpn.api.custom.Subset;
import kpn.core.doc.WithStringId;

import java.util.Optional;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.Builder;
import lombok.NonNull;

/*
  Describes the changes made to a given route in a given changeset.
*/
@Builder
public record RouteChange(
  @NonNull String _id,
  @NonNull ChangeKey key,
  @NonNull ChangeType changeType,
  @NonNull String name,
  @NonNull RouteLocationAnalysis locationAnalysis,
  @NonNull ImmutableList<Ref> addedToNetwork,
  @NonNull ImmutableList<Ref> removedFromNetwork,
  @NonNull Optional<RouteData> before,
  @NonNull Optional<RouteData> after,
  @NonNull ImmutableList<RouteNodeChange> nodeChanges,
  @NonNull ImmutableList<Fact> facts,
  // following values are filled in by RouteChangeAnalyzer.analyzed
  @NonNull Boolean happy,
  @NonNull Boolean investigate,
  @NonNull Boolean impact,
  @NonNull Boolean locationHappy,
  @NonNull Boolean locationInvestigate,
  @NonNull Boolean locationImpact
) implements WithStringId {

  public Long id() {
    return key.elementId();
  }

  public Ref toRef() {
    return new Ref(id(), name);
  }

  public boolean isEmpty() {
    return addedToNetwork.isEmpty() &&
      removedFromNetwork.isEmpty() &&
      facts.isEmpty();
  }

  public ImmutableList<Subset> subsets() {
    return routeDataStream()
      .flatMap(data -> data.subsets().stream())
      .distinct()
      .sorted()
      .collect(ImmutableList.toImmutableList());
  }

  public ReferencedElements referencedElements() {
    ImmutableSet<Long> routeIds = ImmutableSet.of(id());
    ImmutableSet<Long> nodeIds = routeDataStream()
      .flatMap(data -> data.networkNodes().stream())
      .map(RouteNode::nodeId)
      .collect(ImmutableSet.toImmutableSet());
    return new ReferencedElements(nodeIds, routeIds);
  }

  private Stream<RouteData> routeDataStream() {
    return Stream.concat(before.stream(), after.stream());
  }
}
