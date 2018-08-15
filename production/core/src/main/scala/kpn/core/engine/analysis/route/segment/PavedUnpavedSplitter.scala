package kpn.core.engine.analysis.route.segment

import scala.annotation.tailrec

case class SurfaceSegmentFragment(surface: String, segmentFragment: SegmentFragment)


object PavedUnpavedSplitter {

  def split(segmentFragments: Seq[SegmentFragment]): Seq[Seq[SegmentFragment]] = {


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
      doSplit(Seq(Seq(surfaceSegmentFragments.head)), surfaceSegmentFragments.tail).map(c => c.map(_.segmentFragment))
    }
  }

  @tailrec
  private def doSplit(found: Seq[Seq[SurfaceSegmentFragment]], remaining: Seq[SurfaceSegmentFragment]): Seq[Seq[SurfaceSegmentFragment]] = {
    if (remaining.isEmpty) {
      found
    }
    else {
      val lastFound = found.last.last
      if (lastFound.surface == remaining.head.surface) {
        val newFound: Seq[Seq[SurfaceSegmentFragment]] = found.take(found.size - 1) ++ Seq(found.last :+ remaining.head)
        doSplit(newFound, remaining.tail)
      }
      else {
        val newFound: Seq[Seq[SurfaceSegmentFragment]] = found ++ Seq(Seq(remaining.head))
        doSplit(newFound, remaining.tail)
      }
    }
  }

}
