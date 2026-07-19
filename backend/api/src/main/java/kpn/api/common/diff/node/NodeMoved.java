package kpn.api.common.diff.node;

import kpn.api.common.LatLonImpl;

public record NodeMoved(
  LatLonImpl before,
  LatLonImpl after,
  Long distance
) {
}

/*
package kpn.api.common.diff.node

import kpn.api.common.LatLonImpl

case class NodeMoved(before: LatLonImpl, after: LatLonImpl, distance: Long)

*/
