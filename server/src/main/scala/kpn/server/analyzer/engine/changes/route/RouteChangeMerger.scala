package kpn.server.analyzer.engine.changes.route

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.diff.RouteData
import kpn.core.util.Log

class RouteChangeMerger(left: RouteChange, right: RouteChange) {

  private val log = Log(classOf[RouteChangeMerger])

  def merged: RouteChange = {
    if (left == right) {
      left
    }
    else {
      assertFixedFields(left, right)
      RouteChangeStateAnalyzer.analyzed(
        RouteChange(
          left.key.toId,
          left.key,
          mergedChangeType(),
          left.name,
          locationAnalysis = left.locationAnalysis,
          mergedRefs(left.addedToNetwork, right.addedToNetwork),
          mergedRefs(left.removedFromNetwork, right.removedFromNetwork),
          before = mergedRouteData(left.before, right.before),
          after = mergedRouteData(left.after, right.after),
          removedWays = left.removedWays, // TODO CHANGE
          addedWays = left.addedWays, // TODO CHANGE
          updatedWays = left.updatedWays, // TODO CHANGE
          diffs = left.diffs, // TODO CHANGE
          facts = (left.facts ++ right.facts).distinct.sortBy(_.name)
        )
      )
    }
  }

  private def assertFixedFields(left: RouteChange, right: RouteChange): Unit = {
    if (left.key != right.key) {
      log.info(s"Route keys do not match: ${left.key} != ${right.key}")
    }

    if (left.before.nonEmpty && right.before.nonEmpty) {
      if (left.before.get != right.before.get) {
        log.info(s"Route 'before' do not match: ${left.before} != ${right.before}")
      }
    }

    if (left.after.nonEmpty && right.after.nonEmpty) {
      if (left.after.get != right.after.get) {
        log.info(s"Route 'after' do not match: ${left.before} != ${right.before}")
      }
    }
  }

  private def mergedChangeType(): ChangeType = {
    if (left.changeType == right.changeType) {
      left.changeType
    }
    else {
      log.info(s"Node changeTypes do not match: ${left.changeType} != ${right.changeType}")
      if (left.changeType == ChangeType.Create || right.changeType == ChangeType.Create) {
        ChangeType.Create
      }
      else if (left.changeType == ChangeType.Delete || right.changeType == ChangeType.Delete) {
        ChangeType.Delete
      }
      else {
        left.changeType
      }
    }
  }

  private def mergedRouteData(leftRouteData: Option[RouteData], rightRouteData: Option[RouteData]): Option[RouteData] = {
    if (leftRouteData.isEmpty && rightRouteData.isEmpty) {
      None
    }
    else if (leftRouteData.nonEmpty && rightRouteData.isEmpty) {
      leftRouteData
    }
    else if (leftRouteData.isEmpty && rightRouteData.nonEmpty) {
      rightRouteData
    }
    else {
      leftRouteData
    }
  }

  private def mergedRefs(leftRefs: Seq[Ref], rightRefs: Seq[Ref]): Seq[Ref] = {
    if (leftRefs == rightRefs) {
      leftRefs
    }
    else if (leftRefs.isEmpty) {
      rightRefs
    }
    else if (rightRefs.isEmpty) {
      leftRefs
    }
    else {
      (leftRefs ++ rightRefs).distinct.sortBy(_.id)
    }
  }
}
