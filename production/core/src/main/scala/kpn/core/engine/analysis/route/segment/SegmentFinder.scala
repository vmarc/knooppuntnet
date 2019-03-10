package kpn.core.engine.analysis.route.segment

import kpn.core.common.Timer
import kpn.core.engine.analysis.NodeFragmentConnectionAnalyzer
import kpn.core.engine.analysis.route.RouteNode
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.data.Node

class SegmentFinderAbort() extends RuntimeException

case class SegmentFinderContext(
  timer: Timer,
  availableFragments: Seq[Fragment],
  sourceNode: Node,
  targetNode: Node,
  indent: Int,
  direction: SegmentDirection.Value,
  node: Node,
  usedFragments: Seq[Fragment] = Seq.empty,
  currentSegmentFragments: Seq[SegmentFragment] = Seq.empty,
  potentialSolutionCount: Long = 0
)

class SegmentFinder(networkType: NetworkType, allRouteNodes: Set[RouteNode], allNodes: Set[Node]) {

  private val maxSolutionCount = 1000
  private val timeout = 2000L

  private val log = Log(classOf[SegmentFinder])

  def find(
    availableFragments: Seq[Fragment],
    direction: SegmentDirection.Value,
    source: Node,
    target: Node
  ): Option[Segment] = {

    val context = SegmentFinderContext(
      new Timer(timeout),
      availableFragments,
      source,
      target,
      0,
      direction,
      source
    )

    recursiveFindSegments(context)
  }

  private def recursiveFindSegments(context: SegmentFinderContext): Option[Segment] = {

    val remainingFragments: Set[Fragment] = context.availableFragments.toSet -- context.usedFragments.toSet
    if (remainingFragments.isEmpty) {
      if (context.currentSegmentFragments.nonEmpty) {
        Some(buildSegment(context.currentSegmentFragments, broken = true))
      }
      else {
        None
      }
    }
    else {

      val connectableFragments = remainingFragments.filter { f =>
        val res = canConnect(context.indent, context.direction, context.node, f)
        if (res) {
          if (context.usedFragments.contains(f)) {
            //noinspection SideEffectsInMonadicTransformation
            log.warn("STOP SELF REFERENCING ROUTE ???")
          }
          if (context.currentSegmentFragments.map(_.fragment).contains(f)) {
            //noinspection SideEffectsInMonadicTransformation
            log.warn("STOP SELF REFERENCING ROUTE ???")
          }
        }
        res
      }.toSeq.map { fragment =>
        val reversed = context.node == fragment.nodes.last
        SegmentFragment(fragment, reversed)
      }

      if (connectableFragments.isEmpty) {
        if (log.isDebugEnabled) {
          debug(context.indent, "dead end: no connectable fragments found")
        }
        if (context.currentSegmentFragments.nonEmpty) {
          Some(buildSegment(context.currentSegmentFragments, broken = true))
        }
        else {
          None
        }
      }
      else {

        val newPotentialSolutionCount = context.potentialSolutionCount * connectableFragments.size

        if (log.isDebugEnabled) {
          val dir = context.direction
          val from = context.node.id
          val to = connectableFragments.map(sf => sf.endNode.id).mkString(",")
          //noinspection SideEffectsInMonadicTransformation
          debug(context.indent, s"${context.direction} from $from to $to (potentialSolutionCount=$newPotentialSolutionCount)")
        }
        if (context.potentialSolutionCount > maxSolutionCount) {
          log.warn("Stopped finding segments because the maximum number of allowed possible solutions (" + maxSolutionCount + ") has been exceeded: " + context.potentialSolutionCount + " (analysis would take too long or not end)")
          throw new SegmentFinderAbort()
        }

        val timerState = context.timer.poll()

        if (timerState.isElapsed) {
          val cpuTimeElapsed = timerState.cpuElapsed match {
            case None => ""
            case Some(millis) => s" (cpu time ${millis}ms)"
          }
          log.warn(s"Timeout after ${timerState.epochElapsed}ms$cpuTimeElapsed while finding segments (analysis takes too long)")
          throw new SegmentFinderAbort()
        }

        val passedNodes = context.usedFragments.flatMap(fragment => fragment.nodes).filterNot(_ == context.node)

        val segments: Seq[Segment] = connectableFragments.flatMap { segmentFragment =>

          val path = context.currentSegmentFragments :+ segmentFragment

          if (log.isDebugEnabled) {
            val from = context.node.id
            val to = segmentFragment.endNode.id
            //noinspection SideEffectsInMonadicTransformation
            debug(context.indent + 1, s"${context.direction} from $from to $to path=$path")
          }
          if (passedNodes.contains(segmentFragment.endNode)) {
            if (log.isDebugEnabled) {
              //noinspection SideEffectsInMonadicTransformation
              debug(context.indent, "dead end: already passed through node " + segmentFragment.endNode)
            }
            Some(buildSegment(context.currentSegmentFragments, broken = true))
          }
          else if (context.targetNode == segmentFragment.endNode) {
            val newSegment = buildSegment(path)
            if (log.isDebugEnabled) {
              //noinspection SideEffectsInMonadicTransformation
              debug(context.indent, "found main segment (potentialSolutionCount=" + context.potentialSolutionCount + "): " + new SegmentFormatter(newSegment).string)
            }
            Some(newSegment)
          }
          else if (allNodes.contains(segmentFragment.endNode)) {
            if (log.isDebugEnabled) {
              //noinspection SideEffectsInMonadicTransformation
              debug(context.indent, "dead end: encountered node other than target node")
            }
            None
          }
          else {
            val segmentFragments = context.currentSegmentFragments :+ segmentFragment
            val newContext = context.copy(
              indent = context.indent + 2,
              node = segmentFragment.endNode,
              usedFragments = context.usedFragments ++ segmentFragments.map(_.fragment),
              currentSegmentFragments = context.currentSegmentFragments :+ segmentFragment,
              potentialSolutionCount = newPotentialSolutionCount
            )
            recursiveFindSegments(newContext)
          }
        }

        SegmentSelector.select(segments)
      }
    }
  }

  private def canConnect(indent: Int, direction: SegmentDirection.Value, node: Node, fragment: Fragment): Boolean = {
    val connect = new NodeFragmentConnectionAnalyzer(networkType, direction, node, fragment).canConnect
    val startNode = fragment.nodes.head
    val endNode = fragment.nodes.last
    debug(indent, direction, node, fragment, startNode, endNode, connect)
    connect
  }

  private def buildSegment(segmentFragments: Seq[SegmentFragment], broken: Boolean = false): Segment = {
    if (segmentFragments.isEmpty) {
      Segment(None, None, Seq(), broken)
    }
    else {
      val startNode = segmentFragments.head.startNode
      val endNode = segmentFragments.last.endNode
      val start: Option[RouteNode] = allRouteNodes.find(routeNode => routeNode.node == startNode)
      val end: Option[RouteNode] = allRouteNodes.find(routeNode => routeNode.node == endNode)
      Segment(start, end, segmentFragments, broken)
    }
  }

  private def debug(indent: Int, message: String): Unit = {
    val spaces = (0 to indent).map(x => "  ").mkString
    log.debug(spaces + message)
  }

  private def debug(indent: Int, direction: SegmentDirection.Value, node: Node, fragment: Fragment, startNode: Node, endNode: Node, connect: Boolean): Unit = {
    if (log.isDebugEnabled) {
      val text = if (connect) "can connect   " else "cannot connect"
      val via = new FragmentFormatter(fragment).string
      val message = s"${direction.toString} $text from ${node.id} via $via (start=${startNode.id}, end=${endNode.id})"
      debug(indent, message)
    }
  }
}
