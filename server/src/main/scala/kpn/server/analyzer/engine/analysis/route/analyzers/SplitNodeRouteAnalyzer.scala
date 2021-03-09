package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.FragmentMap
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

  private val fragmentMap: FragmentMap = contextFragmentMap()

  def analyze: RouteAnalysisContext = {

    if (routeNodeAnalysis.redundantNodes.nonEmpty) {
      context
    }
    else {
      val nodeNames =  routeNodeAnalysis.routeNodes.map(_.name)
      val allNodesIdentical = nodeNames.forall(_ == nodeNames.head)
      if (routeNodeAnalysis.routeNodes.size < 2 || !allNodesIdentical) {
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
      if (fragmentId < fragmentMap.size) {
        fragmentMap(fragmentId).nodes.head.id == nodeId
      }
      else {
        false
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
    contextFragmentMap().ids.filterNot(usedFragmentIds.contains)
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
      if (fragmentId < fragmentMap.size) {
        fragmentMap(fragmentId).start.isDefined
      }
      else {
        false
      }
    }
    id.map(fragmentId => SegmentFragment(fragmentMap(fragmentId) /* TODO handle reversed */))
  }

  private def routeNodeAnalysis: RouteNodeAnalysis = {
    context.routeNodeAnalysis.getOrElse(throw new IllegalStateException("RouteNodeAnalysis required before circular route analysis"))
  }

  private def contextFragmentMap(): FragmentMap = {
    context.fragmentMap.getOrElse(throw new IllegalStateException("fragmentMap required before circular route analysis"))
  }

}
