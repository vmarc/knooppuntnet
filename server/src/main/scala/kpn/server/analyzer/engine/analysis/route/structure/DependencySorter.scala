package kpn.server.analyzer.engine.analysis.route.structure

import scala.annotation.tailrec

object DependencySorter {
  def sort(dependencies: Seq[RouteDependency]): Seq[Long] = {
    new DependencySorter().sort(dependencies)
  }
}

class DependencySorter {

  def sort(dependencies: Seq[RouteDependency]): Seq[Long] = {
    sortDependencies(Seq.empty, dependencies)
  }

  @tailrec
  private def sortDependencies(relationIds: Seq[Long], remainingDependencies: Seq[RouteDependency]): Seq[Long] = {
    if (remainingDependencies.isEmpty) {
      relationIds
    }
    else {
      val remainingRelationIds = remainingDependencies.map(_.parentRelationId).distinct
      val withChildrenRelationIds = remainingDependencies.filter(dep =>
        remainingRelationIds.contains(dep.childRelationId)
      ).map(_.parentRelationId)
      val withoutChildrenRelationIds = remainingRelationIds.filterNot(id => withChildrenRelationIds.contains(id))
      val nextRemainingDependencies = remainingDependencies.filterNot(dep =>
        withoutChildrenRelationIds.contains(dep.parentRelationId)
      )
      sortDependencies(relationIds ++ withoutChildrenRelationIds, nextRemainingDependencies)
    }
  }
}
