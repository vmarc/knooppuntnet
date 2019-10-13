package kpn.server.analyzer.engine.analysis

import kpn.core.analysis.BreakPoint
import kpn.core.util.Log
import kpn.core.util.Triplet
import kpn.shared.data.Node
import kpn.shared.data.Way

import scala.annotation.tailrec

/**
 * Finds the first point in a collection of ways where the path is broken.
 */
class BreakPointAnalysis {

  private val log = Log(classOf[BreakPointAnalysis])

  def breakPoint(ways: Seq[Way]): Option[BreakPoint] = {

    val wayString = ways.map(_.id).mkString("+")
    log.debug(s"breakPoint(ways = $wayString)")

    val result = doBreakPoint(ways)
    val resultString = result.map(bp => s"BreakPoint(way=${bp.way.id}, node=${bp.node.id})")
    log.debug("  result=" + resultString)
    result
  }

  private def doBreakPoint(ways: Seq[Way]): Option[BreakPoint] = {
    val waysWithNodes = ways.filter(_.nodes.nonEmpty)

    if (log.isDebugEnabled) {
      if (waysWithNodes != ways) {
        log.debug("  there are ways without nodes")
        log.debug("  waysWithNodes=" + waysWithNodes.map(_.id).mkString("+"))
      }
    }

    if (waysWithNodes.size < 2) {
      None
    }
    else {
      val triplets = Triplet.slide(waysWithNodes).map(t => WayTriplet(t))
      breakPoint(triplets, Nodes())
    }
  }


  @tailrec
  private def breakPoint(triplets: Seq[WayTriplet], candidateCommonNodes: Nodes): Option[BreakPoint] = {

    if(triplets.isEmpty) {
      None
    }
    else {
      triplets.head match {

        case tt: WayTriplet =>

          val newCandidateCommonNodesOrBreakPoint: Either[Nodes, BreakPoint] = tt.delegate.previous match {
            case Some(previousWay) => findBreakPoint(previousWay, tt.delegate.current, tt.delegate.next, candidateCommonNodes)
            case None => Left(endNodes(tt.delegate.current))
          }

          newCandidateCommonNodesOrBreakPoint match {
            case Left(newCandidateCommonNodes) => breakPoint(triplets.tail, newCandidateCommonNodes)
            case Right(breakPoint) =>
              Some(breakPoint)
          }
      }
    }
  }

  private def findBreakPoint(previousWay: Way, currentWay: Way, next: Option[Way], candidateCommonNodes: Nodes): Either[Nodes, BreakPoint] = {
    val nodeCommonWithPreviousWay = endNodes(currentWay).delegate.find(node => candidateCommonNodes.delegate.contains(node))
    nodeCommonWithPreviousWay match {
      case Some(node) => Left(Nodes((endNodes(currentWay).delegate.toSet -- Set(node)).toSeq))
      case None =>
        if (WayAnalyzer.isRoundabout(currentWay) || (currentWay.nodes.size > 2 && WayAnalyzer.isClosedLoop(currentWay))) {
          Right(BreakPoint(previousWay, candidateCommonNodes.delegate.last))
        }
        else {
          Right(BreakPoint(currentWay, breakPointNode(currentWay, next)))
        }
    }
  }

  private def breakPointNode(currentWay: Way, next: Option[Way]): Node = {
    next match {
      case None => currentWay.nodes.head
      case Some(nextWay) =>
        if (endNodes(nextWay).delegate.contains(currentWay.nodes.head)) {
          currentWay.nodes.last
        }
        else {
          currentWay.nodes.head
        }
    }
  }

  private def endNodes(way: Way): Nodes = {
    if (WayAnalyzer.isRoundabout(way) || WayAnalyzer.isClosedLoop(way)) {
      Nodes(way.nodes)
    }
    else {
      Nodes(Seq(way.nodes.head, way.nodes.last))
    }
  }
}
