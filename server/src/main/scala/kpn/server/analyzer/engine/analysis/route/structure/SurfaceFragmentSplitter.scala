package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.NetworkType

import scala.annotation.tailrec

object SurfaceFragmentSplitter {

  def split(networkTypes: Seq[NetworkType], segmentFragments: Seq[NewRouteSegmentElementFragment]): Seq[NewRouteSegmentElementFragmentGroup] = {

    if (segmentFragments.isEmpty) {
      Seq.empty
    }
    else {
      val firstFragment = segmentFragments.head
      val initialSegment = NewRouteSegmentElementFragmentGroup(firstFragment.surface, Seq(firstFragment))
      doSplit(Seq(initialSegment), segmentFragments.tail)
    }
  }

  @tailrec
  private def doSplit(found: Seq[NewRouteSegmentElementFragmentGroup], remaining: Seq[NewRouteSegmentElementFragment]): Seq[NewRouteSegmentElementFragmentGroup] = {
    if (remaining.isEmpty) {
      found
    }
    else {
      val surface = remaining.head.surface
      val fragment = remaining.head
      val lastSegment = found.last
      if (lastSegment.surface == surface) {
        val updatedSegment = lastSegment.copy(fragments = lastSegment.fragments :+ fragment)
        val newFound = found.take(found.size - 1) :+ updatedSegment
        doSplit(newFound, remaining.tail)
      }
      else {
        val newSegment = NewRouteSegmentElementFragmentGroup(surface, Seq(fragment))
        doSplit(found :+ newSegment, remaining.tail)
      }
    }
  }
}
