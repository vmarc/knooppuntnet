package kpn.server.analyzer.engine.monitor.domain

case class SuperSegmentElement(relationSegment: MonitorRouteRelationSegment, reversed: Boolean = false) {

  def summary: String = s"${relationSegment.id} start=${relationSegment.startNodeId}, end=${relationSegment.endNodeId}, reversed=$reversed"

  def startNodeId: Long = {
    if (reversed) {
      relationSegment.segment.endNodeId
    }
    else {
      relationSegment.segment.startNodeId
    }
  }

  def endNodeId: Long = {
    if (reversed) {
      relationSegment.segment.startNodeId
    }
    else {
      relationSegment.segment.endNodeId
    }
  }
}

