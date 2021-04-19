package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Node
import kpn.api.common.route.Both
import kpn.api.custom.NetworkType
import kpn.core.common.Timer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.OneWayAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteNode

import scala.annotation.tailrec

class SegmentFinderAbort() extends RuntimeException

case class SegmentFinderContext(
  timer: Timer,
  availableFragmentIds: Set[Int],
  sourceNode: Node,
  targetNode: Node,
  indent: Int,
  direction: SegmentDirection.Value,
  node: Node,
  usedFragmentIds: Set[Int] = Set.empty,
  currentSegmentFragments: Seq[SegmentFragment] = Seq.empty,
  potentialSolutionCount: Long = 0
)

class SegmentFinder(
  fragmentMap: FragmentMap,
  networkType: NetworkType,
  allRouteNodes: Set[RouteNode],
  allNodes: Set[Node],
  loop: Boolean
) {

  private val maxSolutionCount = 1000
  private val timeout = 10000L

  private val log = Log(classOf[SegmentFinder])

  def composeAsIsPath(): Option[Path] = {
    val fragments = fragmentMap.ids.sorted.map(id => fragmentMap(id))
    if (fragments.isEmpty) {
      None
    }
    else {
      composeAsIsFragments(fragments) match {
        case Some(path) => Some(path)
        case None => composeAsIsFragments(fragments.reverse)
      }
    }
  }

  private def composeAsIsFragments(fragments: Vector[Fragment]) = {
    val fragment = fragments.head
    fragment.start match {
      case None => None
      case Some(start) =>
        val reversedOption = if (start.id == fragment.nodes.head.id) {
          Some(false)
        }
        else if (start.id == fragment.nodes.last.id) {
          Some(true)
        }
        else {
          None
        }
        reversedOption match {
          case None => None
          case Some(reversed) =>
            val segmentFragment = SegmentFragment(fragment, reversed = reversed)
            composeNextFragment(Seq(segmentFragment), fragments.tail)
        }
    }
  }

  @tailrec
  private def composeNextFragment(segments: Seq[SegmentFragment], remainingFragments: Seq[Fragment]): Option[Path] = {

    if (remainingFragments.isEmpty) {
      if (segments.head.startNode.id == segments.last.endNode.id) {
        buildPath(segments)
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
          composeNextFragment(segments :+ segmentFragment, remainingFragments.tail)
      }
    }
  }

  def find(
    availableFragmentIds: Set[Int],
    direction: SegmentDirection.Value,
    source: Node,
    target: Node
  ): Option[Path] = {

    val context = SegmentFinderContext(
      new Timer(timeout),
      availableFragmentIds,
      source,
      target,
      0,
      direction,
      source
    )

    recursiveFindSegments(context)
  }

  private def recursiveFindSegments(context: SegmentFinderContext): Option[Path] = {

    val remainingFragmentIds: Set[Int] = context.availableFragmentIds -- context.usedFragmentIds
    if (remainingFragmentIds.isEmpty) {
      if (context.currentSegmentFragments.nonEmpty) {
        buildPath(context.currentSegmentFragments, broken = true)
      }
      else {
        None
      }
    }
    else {

      val connectableFragments = remainingFragmentIds.filter {
        fragmentId =>
          val res = canConnect(context.indent, context.direction, context.node, fragmentId)
          if (res) {
            if (context.usedFragmentIds.contains(fragmentId)) {
              //noinspection SideEffectsInMonadicTransformation
              log.warn("STOP SELF REFERENCING ROUTE ???")
            }
            if (context.currentSegmentFragments.map(_.fragment.id).contains(fragmentId)) {
              //noinspection SideEffectsInMonadicTransformation
              log.warn("STOP SELF REFERENCING ROUTE ???")
            }
          }
          res
      }.toSeq.map {
        fragmentId =>
          val fragment = fragmentMap(fragmentId)
          val reversed = context.node == fragment.nodes.last
          SegmentFragment(fragment, reversed)
      }

      if (connectableFragments.isEmpty) {
        if (log.isDebugEnabled) {
          debug(context.indent, "dead end: no connectable fragments found")
        }
        if (context.currentSegmentFragments.nonEmpty) {
          buildPath(context.currentSegmentFragments, broken = true)
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
          debug(
            context.indent, s"${context.direction} from $from to $to (potentialSolutionCount=$newPotentialSolutionCount)"
          )
        }
        if (context.potentialSolutionCount > maxSolutionCount) {
          log.warn("Stopped finding segments because the maximum number of allowed possible solutions (" + maxSolutionCount + ") has been exceeded: " + context.potentialSolutionCount + " (analysis would take too long or not end)")
          throw new SegmentFinderAbort()
        }

        val timerState = context.timer.poll()

        if (timerState.isElapsed) {
          val cpuTimeElapsed = timerState.cpuElapsed match {
            case None => ""
            case Some(millis) =>
              s" (cpu time ${millis}ms)"
          }
          log.warn(
            s"Timeout after ${timerState.epochElapsed}ms$cpuTimeElapsed while finding segments (analysis takes too long)"
          )
          throw new SegmentFinderAbort()
        }

        val passedNodes = context.usedFragmentIds.map(id => fragmentMap(id)).flatMap(fragment => fragment.nodes).filterNot(_ == context.node)

        val paths: Seq[Path] = connectableFragments.flatMap {
          segmentFragment =>

            val path = context.currentSegmentFragments :+ segmentFragment

            if (log.isDebugEnabled) {
              val from = context.node.id
              val to = segmentFragment.endNode.id
              //noinspection SideEffectsInMonadicTransformation
              debug(
                context.indent + 1, s"${context.direction} from $from to $to path=$path"
              )
            }
            if (!loop && passedNodes.contains(segmentFragment.endNode)) {
              if (log.isDebugEnabled) {
                //noinspection SideEffectsInMonadicTransformation
                debug(context.indent, "dead end: already passed through node " + segmentFragment.endNode)
              }
              buildPath(context.currentSegmentFragments, broken = true)
            }
            else if (context.targetNode == segmentFragment.endNode) {
              val newPath = buildPath(path)
              if (log.isDebugEnabled && newPath.nonEmpty) {
                //noinspection SideEffectsInMonadicTransformation
                debug(context.indent, "found main segment (potentialSolutionCount=" + context.potentialSolutionCount + "): " + new PathFormatter(newPath.get).string)
              }
              newPath
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
                usedFragmentIds = context.usedFragmentIds ++ segmentFragments.map(_.fragment.id),
                currentSegmentFragments = context.currentSegmentFragments :+ segmentFragment,
                potentialSolutionCount = newPotentialSolutionCount
              )
              recursiveFindSegments(newContext)
            }
        }

        PathSelector.select(paths)
      }
    }
  }

  private def canConnect(indent: Int, direction: SegmentDirection.Value, node: Node, fragmentId: Int): Boolean = {
    val fragment = fragmentMap(fragmentId)
    val connect = new NodeFragmentConnectionAnalyzer(networkType, direction, node, fragment).canConnect
    val startNode = fragment.nodes.head
    val endNode = fragment.nodes.last
    debug(indent, direction, node, fragment, startNode, endNode, connect)
    connect
  }

  private def buildPath(segmentFragments: Seq[SegmentFragment], broken: Boolean = false): Option[Path] = {
    if (segmentFragments.isEmpty) {
      None
    }
    else {
      val startNodeId = segmentFragments.head.startNode.id
      val endNodeId = segmentFragments.last.endNode.id
      val start: Option[RouteNode] = allRouteNodes.find(routeNode => routeNode.node.id == startNodeId)
      val end: Option[RouteNode] = allRouteNodes.find(routeNode => routeNode.node.id == endNodeId)
      val segments = PavedUnpavedSplitter.split(segmentFragments)
      Some(Path(start, end, startNodeId, endNodeId, segments, oneWay(segments), broken))
    }
  }

  private def oneWay(segments: Seq[Segment]): Boolean = {
    segments.exists(_.fragments.exists(fragment => new OneWayAnalyzer(fragment.fragment.way).direction != Both))
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
