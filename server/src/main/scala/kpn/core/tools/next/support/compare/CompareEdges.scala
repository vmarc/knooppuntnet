package kpn.core.tools.next.support.compare

import kpn.api.common.route.RouteEdge
import kpn.core.doc.OldRouteDoc
import kpn.core.doc.RouteDetailDoc
import kpn.core.util.Log

import scala.math.abs

case class CompareEdge(
  sourceNodeId: Long,
  sinkNodeId: Long,
  meters: Long
)

class CompareEdges(oldRouteDoc: OldRouteDoc, newRouteDoc: RouteDetailDoc, log: Log) {
  def compare(): Unit = {
    if (oldRouteDoc.facts.nonEmpty && newRouteDoc.facts.isEmpty && newRouteDoc.edges.nonEmpty) {
      // new analysis without problem found edges, old analysis failed: assume new edges better than old
      return
    }
    if (newRouteDoc.segments.size != 1) {
      // cannot compare edges
      return
    }
    if (oldRouteDoc.edges.isEmpty && newRouteDoc.edges.nonEmpty) {
      // new analysis found edges: assume new edges better than old
      return
    }

    val oldEdges = oldCompareEdges()
    val newEdges = newCompareEdges()
    if (!edgesEqual(oldEdges, newEdges)) {
      val detail = Seq(
        oldRouteDoc.edges.map(edge => s"old-edge $edge"),
        newRouteDoc.edges.map(edge => s"new-edge $edge"),
        oldEdges.map(edge => s"old-edge $edge"),
        newEdges.map(edge => s"new-edge $edge"),
      ).flatten.mkString("\n")
      log.info(s"edge mismatch\n$detail")
    }
  }

  private def newCompareEdges(): Seq[CompareEdge] = {
    sort(newRouteDoc.edges.map(toCompareEdge))
  }

  private def oldCompareEdges(): Seq[CompareEdge] = {
    sort(distinctOldEdges(Seq.empty, oldRouteDoc.edges))
  }

  private def distinctOldEdges(edges: Seq[CompareEdge], remainingEdges: Seq[RouteEdge]): Seq[CompareEdge] = {
    if (remainingEdges.isEmpty) {
      edges
    }
    else {
      val edge = toCompareEdge(remainingEdges.head)
      if (edges.contains(edge)) {
        distinctOldEdges(edges, remainingEdges.tail)
      }
      else {
        distinctOldEdges(edges :+ edge, remainingEdges.tail)
      }
    }
  }

  private def toCompareEdge(edge: RouteEdge): CompareEdge = {
    CompareEdge(
      edge.sourceNodeId,
      edge.sinkNodeId,
      edge.meters
    )
  }

  private def sort(edges: Seq[CompareEdge]): Seq[CompareEdge] = {
    edges.sortBy(edge => edge.sourceNodeId -> edge.sinkNodeId)
  }

  private def edgesEqual(oldEdges: Seq[CompareEdge], newEdges: Seq[CompareEdge]): Boolean = {
    if (oldEdges.size == newEdges.size) {
      val mismatches = oldEdges.filter { oldEdge =>
        !newEdges.exists(newEdge => edgeEqual(oldEdge, newEdge))
      }
      mismatches.isEmpty
    }
    else {
      false
    }
  }

  private def edgeEqual(oldEdge: CompareEdge, newEdge: CompareEdge): Boolean = {
    val metersEqual = abs(oldEdge.meters - newEdge.meters) < (newEdge.meters / 5) // 20%
    if (metersEqual) {
      (oldEdge.sourceNodeId == newEdge.sourceNodeId && oldEdge.sinkNodeId == newEdge.sinkNodeId) ||
        (oldEdge.sourceNodeId == newEdge.sinkNodeId && oldEdge.sinkNodeId == newEdge.sourceNodeId)
    }
    else {
      false
    }
  }
}
