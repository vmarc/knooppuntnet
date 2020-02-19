package kpn.server.analyzer.engine.changes.node

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawNode
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.util.Log

class NodeChangeMerger(left: NodeChange, right: NodeChange) {

  private val log = Log(classOf[NodeChangeMerger])

  def merged: NodeChange = {

    if (left == right) {
      left
    }
    else {
      assertFixedFields(left, right)

      analyzed(
        NodeChange(
          left.key,
          mergedChangeType(),
          mergedSubsets(),
          left.name,
          mergedBefore(),
          mergedAfter(),
          mergedConnectionChanges(),
          mergedRoleConnectionChanges(),
          mergedDefinedInNetworkChanges(),
          mergedTagDiffs(),
          mergedNodeMoved(),
          mergedAddedToRoute(),
          mergedRemovedFromRoute(),
          mergedAddedToNetwork(),
          mergedRemovedFromNetwork(),
          mergedFactDiffs(),
          mergedFacts()
        )
      )
    }
  }

  private def assertFixedFields(left: NodeChange, right: NodeChange): Unit = {
    if (left.key != right.key) {
      log.info(s"Node keys do not match: ${left.key} != ${right.key}")
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

  private def mergedSubsets(): Seq[Subset] = {
    (left.subsets ++ right.subsets).distinct
  }

  private def mergedBefore(): Option[RawNode] = {
    if (left.before.isEmpty && right.before.isEmpty) {
      None
    }
    else if (left.before.nonEmpty && right.before.isEmpty) {
      left.before
    } else if (left.before.isEmpty && right.before.nonEmpty) {
      right.before
    }
    else {
      if (left.before != right.before) {
        log.warn(s"Node 'before' values do not match: ${left.before} != ${right.before}. Continue processing with left value.")
      }
      left.before
    }
  }

  private def mergedAfter(): Option[RawNode] = {
    if (left.after.isEmpty && right.after.isEmpty) {
      None
    }
    else if (left.after.nonEmpty && right.after.isEmpty) {
      left.after
    } else if (left.after.isEmpty && right.after.nonEmpty) {
      right.after
    }
    else {
      if (left.after != right.after) {
        log.warn(s"Node 'after' values do not match: ${left.after} != ${right.after}. Continue processing with left value.")
      }
      left.after
    }
  }

  private def mergedConnectionChanges(): Seq[RefBooleanChange] = {
    // TODO CHANGE expand code to make sure there are no duplicate entries per network id
    left.connectionChanges ++ right.connectionChanges
  }

  private def mergedRoleConnectionChanges(): Seq[RefBooleanChange] = {
    // TODO CHANGE expand code to make sure there are no duplicate entries per network id
    left.roleConnectionChanges ++ right.roleConnectionChanges
  }

  private def mergedDefinedInNetworkChanges(): Seq[RefBooleanChange] = {
    // TODO CHANGE expand code to make sure there are no duplicate entries per network id
    left.definedInNetworkChanges ++ right.definedInNetworkChanges
  }

  private def mergedTagDiffs(): Option[TagDiffs] = {
    left.tagDiffs
  }

  private def mergedNodeMoved(): Option[NodeMoved] = {
    left.nodeMoved
  }

  private def mergedAddedToRoute(): Seq[Ref] = {
    (left.addedToRoute ++ right.addedToRoute).distinct.sortBy(_.id)
  }

  private def mergedRemovedFromRoute(): Seq[Ref] = {
    (left.removedFromRoute ++ right.removedFromRoute).distinct.sortBy(_.id)
  }

  private def mergedAddedToNetwork(): Seq[Ref] = {
    (left.addedToNetwork ++ right.addedToNetwork).distinct.sortBy(_.id)
  }

  private def mergedRemovedFromNetwork(): Seq[Ref] = {
    (left.removedFromNetwork ++ right.removedFromNetwork).distinct.sortBy(_.id)
  }

  private def mergedFactDiffs(): FactDiffs = {
    left.factDiffs
  }

  private def mergedFacts(): Seq[Fact] = {
    (left.facts ++ right.facts).distinct.sortBy(_.id)
  }

  private def analyzed(nodeChange: NodeChange): NodeChange = {
    new NodeChangeAnalyzer(nodeChange).analyzed()
  }

}
