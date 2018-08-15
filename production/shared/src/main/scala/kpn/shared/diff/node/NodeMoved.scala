package kpn.shared.diff.node

import kpn.shared.LatLonImpl

case class NodeMoved(before: LatLonImpl, after: LatLonImpl, distance: Int)
