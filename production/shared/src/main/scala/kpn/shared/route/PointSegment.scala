package kpn.shared.route

import kpn.shared.LatLonImpl

case class PointSegment(p1: LatLonImpl, p2: LatLonImpl) {
  def normalized: PointSegment = {
    if (p1.latitude > p2.latitude) {
      PointSegment(p2, p1)
    }
    else {
      this
    }
  }
}
