package kpn.api.common.route;

import kpn.api.common.route.WayLine;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record WayGeometryUpdate(
  Long wayId,
  ImmutableList<WayLine> common,
  ImmutableList<WayLine> added,
  ImmutableList<WayLine> removed
) {
}

/*
package kpn.api.common.route

case class WayGeometryUpdate(
  wayId: Long,
  common: Option[Seq[WayLine]], // blue
  added: Option[Seq[WayLine]], // green
  removed: Option[Seq[WayLine]], // red
)

*/
