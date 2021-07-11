package kpn.api.common.common

case class TrackPath(
  pathId: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  oneWay: Boolean,
  segments: Seq[TrackSegment]
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("pathId", pathId).
    field("startNodeId", startNodeId).
    field("endNodeId", endNodeId).
    field("meters", meters).
    field("oneWay", oneWay).
    field("segments", segments).
    build

  def trackPoints: Seq[TrackPoint] = {
    segments.headOption match {
      case None => Seq.empty
      case Some(segment) => segment.source +: segments.flatMap(segment => segment.fragments.map(_.trackPoint))
    }
  }

  def reverse: TrackPath = {

    val reversedSegments = segments.reverse.map { segment =>
      val source = segment.trackPoints.last
      val trackPoints = segment.trackPoints.dropRight(1).reverse
      val reversedFragments = trackPoints.zip(segment.fragments.reverse).map { case (trackPoint, fragment) =>

        val newOrientation = (fragment.orientation + 180) % 360

        TrackSegmentFragment(
          trackPoint,
          fragment.meters,
          newOrientation,
          fragment.streetIndex
        )
      }

      TrackSegment(
        segment.surface,
        source,
        reversedFragments
      )
    }

    TrackPath(
      pathId = pathId,
      startNodeId = endNodeId,
      endNodeId = startNodeId,
      meters = meters,
      oneWay = oneWay,
      segments = reversedSegments
    )
  }

}
