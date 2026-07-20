package kpn.api.common.route;

import kpn.api.common.route.WayLine;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record WayGeometryUpdate(
  Long wayId,
  ImmutableList<WayLine> common, // blue
  ImmutableList<WayLine> added, // green
  ImmutableList<WayLine> removed // red
) {
}
