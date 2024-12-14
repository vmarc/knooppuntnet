package kpn.server.analyzer.engine.changes.diff

import kpn.api.common.common.ReferencedElements
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.server.analyzer.engine.analysis.route.RouteDetailAnalysis

object RouteDiffs {
  def empty: RouteDiffs = RouteDiffs()
}

case class RouteDiffs(
  removed: Seq[RouteDetailAnalysis] = Seq.empty,
  added: Seq[RouteDetailAnalysis] = Seq.empty,
  updated: Seq[RouteUpdate] = Seq.empty
) {

  def subsets: Seq[Subset] = (removed.flatMap(_.subsets) ++ added.flatMap(_.subsets) ++ updated.flatMap(_.subsets)).distinct.sorted

  def nonEmpty: Boolean = removed.nonEmpty || added.nonEmpty || updated.nonEmpty

  def referencedElements: ReferencedElements = {
    val routeIds = removed.map(_.id) ++ added.map(_.id) ++ updated.map(_.id)
    val e1 = ReferencedElements.merge(updated.map(_.referencedElements): _*)
    val e2 = ReferencedElements(routeIds = routeIds.toSet)
    ReferencedElements.merge(e1, e2)
  }

  def toRefDiffs: RefDiffs = RefDiffs(
    removed.map(_.toRef),
    added.map(_.toRef),
    updated.map(_.toRef)
  )
}
