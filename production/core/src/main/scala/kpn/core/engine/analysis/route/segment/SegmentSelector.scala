package kpn.core.engine.analysis.route.segment

/*
 * Selects the shortest segment that is not broken. If there are no segments
 * that are not broken: selects the longest broken segment.
 */
object SegmentSelector {

  def select(segments: Seq[Segment]): Option[Segment] = {
    val okSegments = segments.filterNot(_.broken)

    if (okSegments.nonEmpty) {
      if(okSegments.size == 1) {
        Some(okSegments.head)
      }
      else {
        Some(sorted(okSegments).head) // shortest
      }
    }
    else {
      val brokenSegments = segments.filter(_.broken)
      if (brokenSegments.nonEmpty) {
        if(brokenSegments.size == 1) {
          Some(brokenSegments.head)
        }
        else {
          Some(sorted(brokenSegments).last) // longest
        }
      }
      else {
        None
      }
    }
  }

  private def sorted(segments: Seq[Segment]): Seq[Segment] = {
    val s1 = segments.map(segment => segment.meters -> segment)
    val s2 = s1.sortBy(_._1)
    val s3 = s2.map(_._2)
    s3
  }
}
