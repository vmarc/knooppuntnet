package kpn.api.common.planner;

import kpn.api.common.common.TrackPathKey;
import kpn.core.doc.Storable;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record LegEndRoute(
  ImmutableList<TrackPathKey> trackPathKeys,
  Optional<TrackPathKey> selection
) implements Storable {}
