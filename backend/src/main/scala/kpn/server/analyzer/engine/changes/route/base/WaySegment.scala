package kpn.server.analyzer.engine.changes.route.base

import kpn.server.domain.StringCoordinate

case class WaySegment(c1: StringCoordinate, c2: StringCoordinate) {
  def normalized: WaySegment = {
    if (c1.x > c2.x) {
      WaySegment(c2, c1)
    }
    else {
      this
    }
  }
}
