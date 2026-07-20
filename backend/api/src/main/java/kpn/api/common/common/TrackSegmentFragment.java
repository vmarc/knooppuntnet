package kpn.api.common.common;

import kpn.api.common.common.TrackPoint;

public record TrackSegmentFragment(
  TrackPoint trackPoint,
  Long meters
) {}
