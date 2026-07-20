package kpn.api.common.route;

import kpn.api.common.LatLonImpl;

public record PointSegment(
  LatLonImpl p1,
  LatLonImpl p2
) {}

/* TODO migrate

  def normalized: PointSegment = {
    if (p1.latitude > p2.latitude) {
      PointSegment(p2, p1)
    }
    else {
      this
    }
  }

*/
