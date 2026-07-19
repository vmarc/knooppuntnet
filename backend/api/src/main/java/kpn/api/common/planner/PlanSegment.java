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

/*
package kpn.api.common.planner

case class PlanSegment(
  meters: Long,
  surface: String,
  colour: Option[String],
  fragments: Seq[PlanFragment]
) {
}

*/
