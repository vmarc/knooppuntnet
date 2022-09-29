package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.analysis.route.RouteNode

import scala.annotation.tailrec

class AsIsLoopPathBuilder(
  networkType: NetworkType,
  fragmentMap: FragmentMap,
  allRouteNodes: Set[RouteNode],
) {

  def build(): Option[Path] = {
    if (fragmentMap.isEmpty) {
      None
    }
    else {
      val fragmentsInOriginalOrder = fragmentMap.ids.sorted.map(id => fragmentMap(id))
      buildPath(fragmentsInOriginalOrder) match {
        case Some(path) => Some(path)
        case None => buildPath(fragmentsInOriginalOrder.reverse)
      }
    }
  }

  private def buildPath(fragments: Vector[Fragment]): Option[Path] = {
    val fragment = fragments.head
    if (fragment.start.isDefined) {
      val segmentFragment = SegmentFragment(fragment)
      buildNextFragment(Seq(segmentFragment), fragments.tail)
    }
    else if (fragment.end.isDefined) {
      val segmentFragment = SegmentFragment(fragment, reversed = true)
      buildNextFragment(Seq(segmentFragment), fragments.tail)
    }
    else {
      None
    }
  }

  @tailrec
  private def buildNextFragment(segments: Seq[SegmentFragment], remainingFragments: Seq[Fragment]): Option[Path] = {

    if (remainingFragments.isEmpty) {
      if (segments.head.startNode.id == segments.last.endNode.id) {
        new PathBuilder(allRouteNodes).buildPath(networkType, segments)
      }
      else {
        None
      }
    }
    else {
      val fragment = remainingFragments.head
      val previousEndNode = segments.last.endNode

      val reversedOption = if (previousEndNode.id == fragment.nodes.head.id) {
        Some(false)
      }
      else if (previousEndNode.id == fragment.nodes.last.id) {
        Some(true)
      }
      else {
        None
      }

      reversedOption match {
        case None => None
        case Some(reversed) =>
          val segmentFragment = SegmentFragment(fragment, reversed = reversed)
          buildNextFragment(segments :+ segmentFragment, remainingFragments.tail)
      }
    }
  }
}
