package kpn.server.analyzer.engine.monitor.tryout

import scala.annotation.tailrec

object XxxBuilder {

}

class XxxBuilder(fragmentMap: XxxMap) {

  def segments(availableFragmentIds: Seq[Int]): Seq[XxxSegment] = {
    findSegments(Seq.empty, availableFragmentIds)
  }

  @tailrec
  private def findSegments(foundSegments: Seq[XxxSegment], availableFragmentIds: Seq[Int]): Seq[XxxSegment] = {
    if (availableFragmentIds.isEmpty) {
      foundSegments
    }
    else {
      val segmentFragment = XxxSegmentFragment(fragmentMap(availableFragmentIds.head))
      val remainingFragmentIds = availableFragmentIds.filterNot(id => id == segmentFragment.fragment.id)

      val sfs1 = findFragments(Seq(segmentFragment), remainingFragmentIds, segmentFragment.endNodeId)
      val sfs2 = findFragments(Seq.empty, remaining(remainingFragmentIds, sfs1), segmentFragment.startNodeId)

      val sfs = reverse(sfs2) ++ sfs1

      val segments = Seq(XxxSegment(sfs))
      val newFoundSegments = foundSegments ++ segments

      findSegments(newFoundSegments, remaining(availableFragmentIds, sfs))
    }
  }

  private def remaining(availableFragmentIds: Seq[Int], segmentFragments: Seq[XxxSegmentFragment]): Seq[Int] = {
    val usedFragments = segmentFragments.map(_.fragment.id).toSet
    availableFragmentIds.filterNot(usedFragments.contains)
  }

  private def findFragments(segmentFragments: Seq[XxxSegmentFragment], availableFragmentIds: Seq[Int], nodeId: Long): Seq[XxxSegmentFragment] = {
    val visitedNodeIds = segmentFragments.flatMap(sf => List(sf.startNodeId, sf.endNodeId))
    val connectableFragmentIds = availableFragmentIds.filter(fragmentId => canConnect(visitedNodeIds, nodeId, fragmentId))
    if (connectableFragmentIds.isEmpty) {
      segmentFragments
    }
    else {
      val maxFragments = 1 //if (optimize) 5 else 1
      val segments = connectableFragmentIds.take(maxFragments).map { fragmentId =>
        val fragment = fragmentMap(fragmentId)
        val reversed = nodeId == fragment.segment.startNodeId
        val segmentFragment = XxxSegmentFragment(fragment, reversed)
        val newSegmentFragments = segmentFragments :+ segmentFragment
        val remainingFragments = availableFragmentIds.filterNot(_ == fragmentId)
        findFragments(newSegmentFragments, remainingFragments, segmentFragment.endNodeId)
      }
      // usually 1 or 0, if more than 1 choose the longest (the other one will be picked up later)
      segments.maxBy(length)
    }
  }

  private def length(sfs: Seq[XxxSegmentFragment]): Long = {
    sfs.map(_.fragment.segment.meters).sum
  }

  private def reverse(sfs: Seq[XxxSegmentFragment]): Seq[XxxSegmentFragment] = {
    sfs.reverse.map(sf => XxxSegmentFragment(sf.fragment, !sf.reversed))
  }

  private def canConnect(visitedNodeIds: Seq[Long], nodeId: Long, fragmentId: Int): Boolean = {
    val fragment = fragmentMap(fragmentId)
    val startNodeId = fragment.segment.startNodeId
    val endNodeId = fragment.segment.endNodeId
    nodeId == startNodeId && (!visitedNodeIds.contains(endNodeId)) ||
      nodeId == endNodeId && (!visitedNodeIds.contains(startNodeId))
  }
}
