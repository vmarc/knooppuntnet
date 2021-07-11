package kpn.server.analyzer.engine.analysis.route.segment

import kpn.server.analyzer.engine.analysis.route.WayAnalyzer

import scala.annotation.tailrec

/**
 * Eliminates duplicate fragments.
 * <p>
 * In practice we sometimes see the same roundabout added to the route relation twice,
 * once with the forward role, and once with the backward role. For segment and route
 * analysis this way of including the roundabout in the route is not needed (adding
 * just once and not using any roles is enough).
 * <p>
 * When the roundabout is added twice, the output of the FragmentAnalyzer contains
 * duplicate segments, which we try to eliminate using this filter, so that we can
 * still do a successful analysis.
 */
object FragmentFilter {

  def filter(fragments: Seq[Fragment]): Seq[Fragment] = {
    recursivelyFilter(Seq.empty, fragments)
  }

  @tailrec
  private def recursivelyFilter(foundFragments: Seq[Fragment], remainingFragments: Seq[Fragment]): Seq[Fragment] = {
    if (remainingFragments.isEmpty) {
      foundFragments
    }
    else {
      val fragment = remainingFragments.head
      if (foundFragments.exists(f => isEquivalent(f, fragment))) {
        recursivelyFilter(foundFragments, remainingFragments.tail)
      }
      else {
        recursivelyFilter(foundFragments :+ fragment, remainingFragments.tail)
      }
    }
  }

  private def isEquivalent(fragment1: Fragment, fragment2: Fragment): Boolean = {
    val nodesFragment1 = fragment1.nodes.map(_.id)
    val nodesFragment2 = fragment2.nodes.map(_.id)
    fragment1.way.id == fragment2.way.id &&
      nodesFragment1 == nodesFragment2 &&
      (if (WayAnalyzer.isRoundabout(fragment1.way)) true else fragment1.role == fragment2.role)
  }
}
