package kpn.api.common.changes.details;

import kpn.api.common.ChangeType;
import kpn.api.common.Country;
import kpn.api.common.RouteType;
import kpn.api.common.changes.details.ChangeKey;
import kpn.api.common.common.ReferencedElements;
import kpn.api.common.data.MetaData;
import kpn.api.common.diff.IdDiffs;
import kpn.api.common.diff.RefDiffs;

import java.util.Optional;
import com.google.common.collect.ImmutableSet;

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

  public ReferencedElements referencedElements() {
    return new ReferencedElements(
      ImmutableSet.copyOf(nodes().ids()),
      ImmutableSet.copyOf(routes().ids())
    );
  }
}
