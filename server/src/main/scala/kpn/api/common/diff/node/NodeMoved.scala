package kpn.api.common.diff.node

import kpn.api.common.LatLonImpl

case class NodeMoved(before: LatLonImpl, after: LatLonImpl, distance: Int)
