package kpn.shared.planner

import kpn.shared.LatLonImpl

case class RouteLegFragment(sink: RouteLegNode, meters: Int, latLons: Seq[LatLonImpl])
