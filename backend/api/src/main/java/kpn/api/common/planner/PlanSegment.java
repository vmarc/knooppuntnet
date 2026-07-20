package kpn.api.common.planner;

import kpn.api.common.planner.PlanFragment;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record PlanSegment(
  Long meters,
  String surface,
  Optional<String> colour,
  ImmutableList<PlanFragment> fragments
) {
}
