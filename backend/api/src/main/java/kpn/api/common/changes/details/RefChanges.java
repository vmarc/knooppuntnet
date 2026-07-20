package kpn.api.common.changes.details;

import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record RefChanges(
  ImmutableList<Ref> oldRefs,
  ImmutableList<Ref> newRefs
) {

  public static RefChanges empty() {
    return new RefChanges(
      ImmutableList.of(),
      ImmutableList.of()
    );
  }
}
