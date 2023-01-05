package kpn.server.analyzer.engine.monitor.domain

import kpn.api.common.monitor.MonitorRouteSegmentInfo

case class SuperSegmentElement(relationSegment: MonitorRouteSegmentInfo, reversed: Boolean = false) {

  def summary: String = s"${relationSegment.id} start=${relationSegment.startNodeId}, end=${relationSegment.endNodeId}, reversed=$reversed"

  def startNodeId: Long = {
    if (reversed) {
      relationSegment.endNodeId
    }
    else {
      relationSegment.startNodeId
    }
  }

  def endNodeId: Long = {
    if (reversed) {
      relationSegment.startNodeId
    }
    else {
      relationSegment.endNodeId
    }
  }
}

