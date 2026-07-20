package kpn.api.common.changes;

import kpn.api.common.data.raw.RawNode;
import kpn.api.common.data.raw.RawRelation;
import kpn.api.custom.Change;
import kpn.api.custom.Timestamp;

import java.util.function.Function;
import com.google.common.collect.ImmutableList;

/*
  All information of a given changeset as available in the minute diff file. A changeset
  can be spread over multiple diff files. In that case the information in this object is
  not the complete changeset.
*/
public record ChangeSet(
  Long id,
  Timestamp timestamp,
  Timestamp timestampFrom,
  Timestamp timestampUntil,
  Timestamp timestampBefore,
  Timestamp timestampAfter,
  ImmutableList<Change> changes
) {

  public ImmutableList<RawRelation> relations(ChangeAction action) {
    return elementsByAction(Change::relations, action);
  }

  public ImmutableList<RawNode> nodes(ChangeAction action) {
    return elementsByAction(Change::nodes, action);
  }

  private <T> ImmutableList<T> elementsByAction(
    Function<Change, ImmutableList<T>> elementSelector,
    ChangeAction action
  ) {
    return changes.stream()
      .filter(change -> change.action() == action)
      .flatMap(change -> elementSelector.apply(change).stream())
      .collect(ImmutableList.toImmutableList());
  }
}
