package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.NetworkType
import kpn.server.analyzer.engine.analysis.route.domain
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisFragment
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisFragmentGroup

import scala.annotation.tailrec

object SurfaceFragmentSplitter {

  def split(networkTypes: Seq[NetworkType], segmentFragments: Seq[RouteAnalysisFragment]): Seq[RouteAnalysisFragmentGroup] = {

    if (segmentFragments.isEmpty) {
      Seq.empty
    }
    else {
      val firstFragment = segmentFragments.head
      val initialSegment = domain.RouteAnalysisFragmentGroup(firstFragment.surface, Seq(firstFragment))
      doSplit(Seq(initialSegment), segmentFragments.tail)
    }
  }

  @tailrec
  private def doSplit(found: Seq[RouteAnalysisFragmentGroup], remaining: Seq[RouteAnalysisFragment]): Seq[RouteAnalysisFragmentGroup] = {
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
        val newSegment = domain.RouteAnalysisFragmentGroup(surface, Seq(fragment))
        doSplit(found :+ newSegment, remaining.tail)
      }
    }
  }
}
