package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.analysis.route.segment.Segment
import kpn.server.analyzer.engine.analysis.route.segment.SegmentFragment

import scala.annotation.tailrec

object SplitNodeRouteAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new SplitNodeRouteAnalyzer(context).analyze
  }
}

class SplitNodeRouteAnalyzer(context: RouteAnalysisContext) {

  private val fragmentMap: Map[Int, Fragment] = fragments.map(f => f.id -> f).toMap

  def analyze: RouteAnalysisContext = {

    if (routeNodeAnalysis.redundantNodes.nonEmpty) {
      context
    }
    else {
      val nodes = routeNodeAnalysis.routeNodes
      val nodeNames = nodes.map(_.name)
      val allNodesIdentical = nodeNames.forall(_ == nodeNames.head)
      if (nodes.size < 2 || !allNodesIdentical) {
        context
      }
      else {
        val paths = findPaths(Seq())
        // TODO look if there are still unused fragments, if so: add to structure.unusedSegments
        val structure = RouteStructure(splitNodePaths = paths)
        context.copy(structure = Some(structure))
      }
    }
  }

  private def findFragment(availableFragmentIds: Seq[Int], nodeId: Long): Option[SegmentFragment] = {
    val id = availableFragmentIds.find { fragmentId =>
      fragmentMap(fragmentId) match {
        case fragment => fragment.nodes.head.id == nodeId
        case _ => false
      }
    }
    id.map(fragmentId => SegmentFragment(fragmentMap(fragmentId) /* TODO handle reversed */))
  }

  @tailrec
  private def findPaths(foundPaths: Seq[Path]): Seq[Path] = {
    val availableFragmentIds = determineAvailableFragmentIds(foundPaths)
    findPathInFragments(availableFragmentIds) match {
      case Some(path) => findPaths(foundPaths :+ path)
      case None => foundPaths
    }
  }

  private def determineAvailableFragmentIds(foundPaths: Seq[Path]): Seq[Int] = {
    val usedFragmentIds = foundPaths.flatMap(path => path.segments.flatMap(_.fragments.map(_.fragment.id)))
    fragments.map(_.id).filterNot(usedFragmentIds.contains)
  }

  private def findPathInFragments(availableFragmentIds: Seq[Int]): Option[Path] = {

    findStartFragment(availableFragmentIds) match {
      case Some(startFragment) =>
        val newAvailableFragmentIds = availableFragmentIds.filterNot(_ == startFragment.fragment.id)
        findPath(Seq(startFragment), newAvailableFragmentIds)

      case None => None
    }
  }

  @tailrec
  private def findPath(segmentFragments: Seq[SegmentFragment], availableFragmentIds: Seq[Int]): Option[Path] = {

    val lastFragment = segmentFragments.last

    lastFragment.fragment.`end` match {
      case Some(end) =>

        val start = segmentFragments.head.fragment.start
        val segments: Seq[Segment] = Seq(Segment("paved" /* TODO determine surface */ , segmentFragments))
        val path: Path = Path(
          start = start,
          end = Some(end),
          startNodeId = segmentFragments.head.startNode.id,
          endNodeId = segmentFragments.last.endNode.id,
          segments = segments,
          oneWay = true // TODO determine whether oneWay or not
        )

        Some(path)

      case None =>

        val lastNode = lastFragment.nodes.last
        findFragment(availableFragmentIds, lastNode.id) match {
          case Some(f) => findPath(segmentFragments :+ f, availableFragmentIds.filterNot(_ == f.fragment.id))
          case None => None
        }
    }
  }

  private def findStartFragment(availableFragmentIds: Seq[Int]): Option[SegmentFragment] = {
    val id = availableFragmentIds.find { fragmentId =>
      fragmentMap(fragmentId) match {
        case fragment => fragment.start.isDefined
        case _ => false
      }
    }
    id.map(fragmentId => SegmentFragment(fragmentMap(fragmentId) /* TODO handle reversed */))
  }

  private def routeNodeAnalysis: RouteNodeAnalysis = {
    context.routeNodeAnalysis.getOrElse(throw new IllegalStateException("RouteNodeAnalysis required before circular route analysis"))
  }

  private def fragments: Seq[Fragment] = {
    context.fragments.getOrElse(throw new IllegalStateException("fragments required before circular route analysis"))
  }

}
