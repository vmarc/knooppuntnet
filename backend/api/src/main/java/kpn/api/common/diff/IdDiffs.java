package kpn.api.common.diff;

import com.google.common.collect.ImmutableList;

public record IdDiffs(
  ImmutableList<Long> removed,
  ImmutableList<Long> added,
  ImmutableList<Long> updated
) {

  public static IdDiffs empty() {
    return new IdDiffs(
      ImmutableList.of(),
      ImmutableList.of(),
      ImmutableList.of()
    );
  }

  public ImmutableList<Long> ids() {
    return ImmutableList.<Long>builder()
      .addAll(removed)
      .addAll(added)
      .addAll(updated)
      .build();
  }

  public boolean nonEmpty() {
    return !removed.isEmpty() || !added.isEmpty() || !updated.isEmpty();
  }
}
