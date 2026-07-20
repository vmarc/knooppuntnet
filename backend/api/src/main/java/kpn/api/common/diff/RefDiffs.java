package kpn.api.common.diff;

import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record RefDiffs(
  ImmutableList<Ref> removed,
  ImmutableList<Ref> added,
  ImmutableList<Ref> updated
) {

  public static RefDiffs empty() {
    return new RefDiffs(
      ImmutableList.of(),
      ImmutableList.of(),
      ImmutableList.of()
    );
  }

  public ImmutableList<Long> ids() {
    return ImmutableList.<Long>builder()
      .addAll(removed.stream().map(Ref::id).toList())
      .addAll(added.stream().map(Ref::id).toList())
      .addAll(updated.stream().map(Ref::id).toList())
      .build();
  }

  public boolean nonEmpty() {
    return !removed.isEmpty() || !added.isEmpty() || !updated.isEmpty();
  }

}
