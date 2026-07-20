package kpn.api.common.route;

import kpn.api.common.route.WayLine;

public record WayGeometry(
  Long wayId,
  WayLine line
) {}
