package kpn.api.common.diff.node;

import kpn.api.common.LatLonImpl;

public record NodeMoved(
  LatLonImpl before,
  LatLonImpl after,
  Long distance
) {
}
