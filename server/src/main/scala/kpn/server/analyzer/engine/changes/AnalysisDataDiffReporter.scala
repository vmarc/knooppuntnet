package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.context.ElementIdMap
import kpn.server.analyzer.engine.context.ElementIdSet
import kpn.server.analyzer.engine.context.Watched

class AnalysisDataDiffReporter {

  def report(left: Watched, right: Watched): Seq[String] = {
    val differences = elementIdDiff("Network", left.networks, right.networks) ++
      diff("Route", left.routes, right.routes) ++
      elementIdDiff("Node", left.nodes, right.nodes)

    if (differences.isEmpty) {
      Seq("No differences")
    }
    else {
      differences
    }
  }

  private def diff(title: String, left: ElementIdMap, right: ElementIdMap): Seq[String] = {
    val differences = dataDiff("watched", left, right)
    if (differences.nonEmpty) {
      Seq(s"$title differences:") ++ differences
    } else {
      Seq.empty
    }
  }

  private def dataDiff(title: String, left: ElementIdMap, right: ElementIdMap): Seq[String] = {
    if (left.ids.isEmpty && right.ids.isEmpty) {
      Seq.empty
    } else {

      val leftIds = left.ids.toSet
      val rightIds = right.ids.toSet
      val leftOnlyKeys = leftIds -- rightIds
      val rightOnlyKeys = rightIds -- leftIds
      val commonKeys = rightIds intersect leftIds

      val differingKeys =
        commonKeys.filter(key => left.get(key) != right.get(key))

      val leftOnly = idList("    leftOnly", leftOnlyKeys)
      val rightOnly = idList("    rightOnly", rightOnlyKeys)

      val diffs = differingKeys.toSeq.flatMap { key =>
        val leftElementIds = left.get(key).get
        val rightElementIds = right.get(key).get

        Seq(s"    $title $key") ++
          networkElementDiff("      nodeIds", leftElementIds.nodeIds, rightElementIds.nodeIds) ++
          networkElementDiff("      wayIds", leftElementIds.wayIds, rightElementIds.wayIds) ++
          networkElementDiff("      relationIds", leftElementIds.relationIds, rightElementIds.relationIds)
      }

      if (leftOnly.isEmpty && rightOnly.isEmpty && diffs.isEmpty) {
        Seq.empty
      }
      else {
        Seq("  " + title + ":") ++ leftOnly ++ rightOnly ++ diffs
      }
    }
  }

  private def networkElementDiff(title: String, left: Set[Long], right: Set[Long]): Seq[String] = {

    val leftOnly = left -- right
    val rightOnly = right -- left

    if (leftOnly.isEmpty && rightOnly.isEmpty) {
      Seq.empty
    }
    else {
      Seq(
        idList(title + "LeftOnly", leftOnly),
        idList(title + "RightOnly", rightOnly)
      ).flatten
    }
  }

  private def idList(title: String, ids: Set[Long]): Seq[String] = {
    if (ids.nonEmpty) {
      Seq(s"$title = " + ids.toSeq.sorted.mkString(", "))
    } else {
      Seq.empty
    }
  }

  private def elementIdDiff(title: String, left: ElementIdSet, right: ElementIdSet): Seq[String] = {
    if (left.ids.isEmpty && right.ids.isEmpty) {
      Seq.empty
    }
    else {
      val leftIds = left.ids.toSet
      val rightIds = right.ids.toSet
      val leftOnlyKeys = leftIds -- rightIds
      val rightOnlyKeys = rightIds -- leftIds

      val leftOnly = idList("    leftOnly", leftOnlyKeys)
      val rightOnly = idList("    rightOnly", rightOnlyKeys)

      Seq(
        Seq("  " + title + ":"),
        leftOnly,
        rightOnly
      ).flatten
    }
  }
}
