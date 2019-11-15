package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Node

import scala.annotation.tailrec

/*
 * Builds segments consisting of adjacent fragments.
 */
class SegmentBuilder {

  def segments(fragments: Seq[Fragment]): Seq[Segment] = {
    val optimize = fragments.size < 50
    findSegments(optimize, Seq(), fragments)
  }

  @tailrec
  private def findSegments(optimize: Boolean, foundSegments: Seq[Segment], availableFragments: Seq[Fragment]): Seq[Segment] = {
    if (availableFragments.isEmpty) {
      foundSegments
    }
    else {
      val segmentFragment = SegmentFragment(availableFragments.head)
      val remainingFragments = availableFragments.tail

      val sfs1 = findFragments(optimize, Seq(segmentFragment), remainingFragments, segmentFragment.endNode)
      val sfs2 = findFragments(optimize, Seq(), remaining(remainingFragments, sfs1), segmentFragment.startNode)

      val sfs = reverse(sfs2) ++ sfs1

      val segments = PavedUnpavedSplitter.split(sfs)
      val newFoundSegments = foundSegments ++ segments

      findSegments(optimize, newFoundSegments, remaining(availableFragments, sfs))
    }
  }

  private def remaining(availableFragments: Seq[Fragment], segmentFragments: Seq[SegmentFragment]): Seq[Fragment] = {
    val usedFragments = segmentFragments.map(_.fragment)
    availableFragments.filterNot(f => usedFragments.contains(f))
  }

  private def findFragments(optimize: Boolean, segmentFragments: Seq[SegmentFragment], availableFragments: Seq[Fragment], node: Node): Seq[SegmentFragment] = {
    val visitedNodeIds = segmentFragments.flatMap(sf => List(sf.startNode, sf.endNode)).map(_.id).toSet
    val connectableFragments = availableFragments.filter(fragment => canConnect(visitedNodeIds, node, fragment))
    if (connectableFragments.isEmpty) {
      segmentFragments
    }
    else {
      val maxFragments = if (optimize) 5 else 1
      val segments = connectableFragments.take(maxFragments).map { fragment =>
        val reversed = node.id == fragment.nodes.last.id
        val segmentFragment = SegmentFragment(fragment, reversed)
        val newSegmentFragments = segmentFragments :+ segmentFragment
        val remainingFragments = availableFragments.filterNot(_ == fragment)
        findFragments(optimize, newSegmentFragments, remainingFragments, segmentFragment.endNode)
      }
      // usually 1 or 0, if more than 1 choose the longest (the other one will be picked up later)
      segments.maxBy(length)
    }
  }

  private def length(sfs: Seq[SegmentFragment]): Long = {
    sfs.map(_.fragment.meters).sum
  }

  private def reverse(sfs: Seq[SegmentFragment]): Seq[SegmentFragment] = {
    sfs.reverse.map(sf => SegmentFragment(sf.fragment, !sf.reversed))
  }

  private def canConnect(visitedNodeIds: Set[Long], node: Node, fragment: Fragment): Boolean = {
    val start = fragment.nodes.head.id
    val end = fragment.nodes.last.id
    node.id == start && (!visitedNodeIds.contains(end)) ||
      node.id == end && (!visitedNodeIds.contains(start))
  }
}
