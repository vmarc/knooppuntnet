package kpn.core.engine.changes.diff

import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.shared.Subset
import kpn.shared.common.ReferencedElements
import kpn.shared.diff.RefDiffs

object RouteDiffs {
  def empty: RouteDiffs = RouteDiffs()
}

case class RouteDiffs(
  removed: Seq[RouteAnalysis] = Seq.empty,
  added: Seq[RouteAnalysis] = Seq.empty,
  updated: Seq[RouteUpdate] = Seq.empty
) {

  def subsets: Seq[Subset] = (removed.flatMap(_.subset) ++ added.flatMap(_.subset) ++ updated.flatMap(_.subsets)).distinct.sorted

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
