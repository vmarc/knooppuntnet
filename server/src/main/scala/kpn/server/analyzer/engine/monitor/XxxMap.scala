package kpn.server.analyzer.engine.monitor

object XxxMap {
  def apply(segments: Seq[MonitorRouteSegmentData]): XxxMap = {
    val segmentsWithIds = segments.toVector.zipWithIndex.map { case (segment, index) => segment.copy(id = index) }
    new XxxMap(segmentsWithIds)
  }
}

class XxxMap(segments: Vector[MonitorRouteSegmentData]) {

  def apply(index: Int): MonitorRouteSegmentData = {
    segments(index)
  }

  def size: Int = segments.size

  def isEmpty: Boolean = segments.isEmpty

  def ids: Vector[Int] = segments.map(_.id)

  def all: Vector[MonitorRouteSegmentData] = segments
}

