package kpn.api.common.route

import kpn.api.common.LatLonImpl

case class WayGeometry(id: Long, nodes: Seq[LatLonImpl])
