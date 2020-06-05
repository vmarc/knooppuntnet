package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Node

import scala.annotation.tailrec

/*
 * Builds segments consisting of adjacent fragments.
 */
class SegmentBuilder(fragmentMap: Map[Int, Fragment]) {

  def segments(availableFragmentIds: Set[Int]): Seq[Segment] = {
    val optimize = fragmentMap.size < 50
    findSegments(optimize, Seq(), availableFragmentIds)
  }

  @tailrec
  private def findSegments(optimize: Boolean, foundSegments: Seq[Segment], availableFragmentIds: Set[Int]): Seq[Segment] = {
    if (availableFragmentIds.isEmpty) {
      foundSegments
    }
    else {
      val segmentFragment = SegmentFragment(fragmentMap(availableFragmentIds.head))
      val remainingFragmentIds = availableFragmentIds - segmentFragment.fragment.id

      val sfs1 = findFragments(optimize, Seq(segmentFragment), remainingFragmentIds, segmentFragment.endNode)
      val sfs2 = findFragments(optimize, Seq(), remaining(remainingFragmentIds, sfs1), segmentFragment.startNode)

      val sfs = reverse(sfs2) ++ sfs1

      val segments = PavedUnpavedSplitter.split(sfs)
      val newFoundSegments = foundSegments ++ segments

      findSegments(optimize, newFoundSegments, remaining(availableFragmentIds, sfs))
    }
  }

  private def remaining(availableFragmentIds: Set[Int], segmentFragments: Seq[SegmentFragment]): Set[Int] = {
    val usedFragments = segmentFragments.map(_.fragment.id).toSet
    availableFragmentIds -- usedFragments
  }

  private def findFragments(optimize: Boolean, segmentFragments: Seq[SegmentFragment], availableFragmentIds: Set[Int], node: Node): Seq[SegmentFragment] = {
    val visitedNodeIds = segmentFragments.flatMap(sf => List(sf.startNode, sf.endNode)).map(_.id).toSet
    val connectableFragmentIds = availableFragmentIds.filter(fragmentId => canConnect(visitedNodeIds, node, fragmentId))
    if (connectableFragmentIds.isEmpty) {
      segmentFragments
    }
    else {
      val maxFragments = if (optimize) 5 else 1
      val segments = connectableFragmentIds.take(maxFragments).map { fragmentId =>
        val fragment = fragmentMap(fragmentId)
        val reversed = node.id == fragment.nodes.last.id
        val segmentFragment = SegmentFragment(fragment, reversed)
        val newSegmentFragments = segmentFragments :+ segmentFragment
        val remainingFragments = availableFragmentIds.filterNot(_ == fragmentId)
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

  private def canConnect(visitedNodeIds: Set[Long], node: Node, fragmentId: Int): Boolean = {
    val fragment = fragmentMap(fragmentId)
    val start = fragment.nodes.head.id
    val end = fragment.nodes.last.id
    node.id == start && (!visitedNodeIds.contains(end)) ||
      node.id == end && (!visitedNodeIds.contains(start))
  }
}
