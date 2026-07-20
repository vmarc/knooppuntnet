package kpn.api.common.changes;

import kpn.api.common.ChangeSetSummary;
import kpn.api.common.changes.details.BaseRouteChange;
import kpn.api.common.changes.details.NetworkChange;
import kpn.api.common.changes.details.NodeChange;
import kpn.api.common.changes.details.RouteChange;
import kpn.api.common.common.ReferencedElements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public record ChangeSetData(
  ChangeSetSummary summary,
  ImmutableList<NetworkChange> networkChanges,
  ImmutableList<BaseRouteChange> baseRouteChanges,
  ImmutableList<RouteChange> routeChanges,
  ImmutableList<NodeChange> nodeChanges
) {

  public long changeSetId() {
    return summary.key().changeSetId();
  }

  public boolean happy() {
    return networkChanges.stream().anyMatch(NetworkChange::happy);
  }

  public boolean investigate() {
    return networkChanges.stream().anyMatch(NetworkChange::investigate);
  }

  public boolean hasNoImpact() {
    return !(happy() || investigate());
  }

  public ReferencedElements referencedElements() {
    List<ReferencedElements> all = Stream.of(
        networkChanges.stream().map(NetworkChange::referencedElements),
        routeChanges.stream().map(RouteChange::referencedElements),
        Stream.of(nodeChangesReferencedElements())
      )
      .flatMap(s -> s)
      .toList();
    return ReferencedElements.merge(all);
  }

  private ReferencedElements nodeChangesReferencedElements() {
    ImmutableSet<Long> nodeIds = nodeChanges.stream()
      .map(NodeChange::id)
      .collect(ImmutableSet.toImmutableSet());
    return new ReferencedElements(nodeIds, ImmutableSet.of());
  }
}
