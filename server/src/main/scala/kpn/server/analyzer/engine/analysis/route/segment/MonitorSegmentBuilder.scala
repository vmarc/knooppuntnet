package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Node
import kpn.api.custom.NetworkType
import kpn.core.util.Log

import scala.annotation.tailrec

/*
 * Builds segments consisting of adjacent fragments.
 */
class MonitorSegmentBuilder(
  networkType: NetworkType,
  fragmentMap: FragmentMap,
  pavedUnpavedSplittingEnabled: Boolean = true
) {

  private val log = Log(classOf[MonitorSegmentBuilder])

  private val runAwayAnalysisCountMax = 10000
  private var runAwayAnalysisCount = 0
  private var runAwayAnalysisDetected = false
  private var runAwayAnalysisDetectedCount = 0

  def segments(availableFragmentIds: Seq[Int]): Seq[Segment] = {
    val optimize = true // fragmentMap.size < 50
    findSegments(optimize, Seq.empty, availableFragmentIds)
  }

  @tailrec
  private def findSegments(optimize: Boolean, foundSegments: Seq[Segment], availableFragmentIds: Seq[Int]): Seq[Segment] = {
    if (log.isTraceEnabled) {
      log.trace(s"RESET runAwayAnalysisCount (findSegments())")
    }
    runAwayAnalysisCount = 0
    if (log.isTraceEnabled) {
      log.trace(s"findSegments(): foundSegments=${foundSegments.size}, availableFragment=${availableFragmentIds.size}")
    }
    if (availableFragmentIds.isEmpty) {
      foundSegments
    }
    else {
      val segmentFragment = SegmentFragment(fragmentMap(availableFragmentIds.head))
      val remainingFragmentIds = availableFragmentIds.filterNot(id => id == segmentFragment.fragment.id)

      if (log.isTraceEnabled) {
        log.trace(s"RESET runAwayAnalysisCount (findSegments()2)")
      }
      runAwayAnalysisCount = 0
      val sfs1 = findFragments("1", 0, optimize, Seq(segmentFragment), remainingFragmentIds, segmentFragment.endNode)
      if (log.isTraceEnabled) {
        log.trace(s"RESET runAwayAnalysisCount (findSegments()3)")
      }
      runAwayAnalysisCount = 0
      val sfs2 = findFragments("2", 0, optimize, Seq.empty, remaining(remainingFragmentIds, sfs1), segmentFragment.startNode)

      val sfs = reverse(sfs2) ++ sfs1

      val segments = {
        if (pavedUnpavedSplittingEnabled) {
          PavedUnpavedSplitter.split(networkType, sfs)
        }
        else {
          Seq(Segment("", sfs))
        }
      }
      val newFoundSegments = foundSegments ++ segments

      findSegments(optimize, newFoundSegments, remaining(availableFragmentIds, sfs))
    }
  }

  private def remaining(availableFragmentIds: Seq[Int], segmentFragments: Seq[SegmentFragment]): Seq[Int] = {
    val usedFragments = segmentFragments.map(_.fragment.id).toSet
    availableFragmentIds.filterNot(usedFragments.contains)
  }

  private def findFragments(
    tag: String,
    level: Int,
    optimize: Boolean,
    segmentFragments: Seq[SegmentFragment],
    availableFragmentIds: Seq[Int],
    node: Node
  ): Seq[SegmentFragment] = {

    runAwayAnalysisCount = runAwayAnalysisCount + 1

    if (log.isTraceEnabled) {
      log.trace(s"findFragments($tag): runAwayAnalysisCount=$runAwayAnalysisCount, level=$level, segmentFragments=${segmentFragments.size}, availableFragment=${availableFragmentIds.size}")
    }
    val visitedNodeIds = {
      val visitedStartNodeIds = segmentFragments.map(_.startNode.id).toSet
      val visitedEndNodeIds = segmentFragments.map(_.endNode.id).toSet
      visitedStartNodeIds ++ visitedEndNodeIds
    }

    val connectableFragmentIds = availableFragmentIds.filter(fragmentId => canConnect(visitedNodeIds, node, fragmentId))

    if (runAwayAnalysisCount > runAwayAnalysisCountMax) {
      runAwayAnalysisDetected = true
      runAwayAnalysisDetectedCount = runAwayAnalysisDetectedCount + 1
      log.info(s"RUNAWAY ANALYSIS DETECTED $runAwayAnalysisDetectedCount")
      segmentFragments
    }
    else if (connectableFragmentIds.isEmpty) {
      segmentFragments
    }
    else {
      val maxFragments = if (optimize) 5 else 1
      if (log.isTraceEnabled) {
        log.trace(s"    connectableFragments=${connectableFragmentIds.size}")
      }
      val segments = connectableFragmentIds.take(maxFragments).map { fragmentId =>
        val fragment = fragmentMap(fragmentId)
        val reversed = node.id == fragment.endNodeId
        val segmentFragment = SegmentFragment(fragment, reversed)
        val newSegmentFragments = segmentFragments :+ segmentFragment
        val remainingFragments = availableFragmentIds.filterNot(_ == fragmentId)
        findFragments(tag, level + 1, optimize, newSegmentFragments, remainingFragments, segmentFragment.endNode)
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

  /*
    Returns true if given node can be used to connect to the fragment with given id.
    The node can be connected if it matches the start or end node of given fragment,
    and given node was not visited yet.
   */
  private def canConnect(visitedNodeIds: Set[Long], node: Node, fragmentId: Int): Boolean = {
    val fragment = fragmentMap(fragmentId)
    val startNodeId = fragment.startNodeId
    val endNodeId = fragment.endNodeId
    node.id == startNodeId && (!visitedNodeIds.contains(endNodeId)) ||
      node.id == endNodeId && (!visitedNodeIds.contains(startNodeId))
  }
}
