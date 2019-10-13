package kpn.server.analyzer.engine.analysis.route.segment

import scala.annotation.tailrec

object PavedUnpavedSplitter {

  case class SurfaceSegmentFragment(surface: String, fragment: SegmentFragment)

  def split(segmentFragments: Seq[SegmentFragment]): Seq[Segment] = {

    val surfaceSegmentFragments = segmentFragments.map { segmentFragment =>
      SurfaceSegmentFragment(
        new SurfaceAnalyzer(segmentFragment.fragment.way).surface(),
        segmentFragment
      )
    }

    if (surfaceSegmentFragments.isEmpty) {
      Seq()
    }
    else {
      val firstFragment = surfaceSegmentFragments.head
      val initialSegment = Segment(firstFragment.surface, Seq(firstFragment.fragment))
      doSplit(Seq(initialSegment), surfaceSegmentFragments.tail)
    }
  }

  @tailrec
  private def doSplit(found: Seq[Segment], remaining: Seq[SurfaceSegmentFragment]): Seq[Segment] = {
    if (remaining.isEmpty) {
      found
    }
    else {
      val surface = remaining.head.surface
      val fragment = remaining.head.fragment
      val lastSegment = found.last
      if (lastSegment.surface == surface) {
        val updatedSegment = lastSegment.copy(fragments = lastSegment.fragments :+ fragment)
        val newFound = found.take(found.size - 1) :+ updatedSegment
        doSplit(newFound, remaining.tail)
      }
      else {
        val newSegment = Segment(surface, Seq(fragment))
        doSplit(found :+ newSegment, remaining.tail)
      }
    }
  }

}
