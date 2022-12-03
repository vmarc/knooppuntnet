package kpn.server.analyzer.engine.monitor

case class XxxSegmentFragment(fragment: MonitorRouteSegmentData, reversed: Boolean = false) {

  def startNodeId: Long = {
    if (reversed) {
      fragment.segment.endNodeId
    }
    else {
      fragment.segment.startNodeId
    }
  }

  def endNodeId: Long = {
    if (reversed) {
      fragment.segment.startNodeId
    }
    else {
      fragment.segment.endNodeId
    }
  }
}

