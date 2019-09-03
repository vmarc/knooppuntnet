package kpn.core.engine.changes.node

import kpn.shared.Fact
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange

class NodeChangeAnalyzer(nodeChange: NodeChange) {

  def analyzed(): NodeChange = {
    nodeChange.copy(happy = happy(), investigate = investigate())
  }

  private def happy(): Boolean = {

    if (nodeChange.changeType == ChangeType.InitialValue) {
      return false
    }

    if (nodeChange.changeType == ChangeType.Create) {
      return true
    }

    nodeChange.definedInNetworkChanges.exists(_.after) ||
      nodeChange.addedToRoute.nonEmpty ||
      nodeChange.addedToNetwork.nonEmpty ||
      nodeChange.factDiffs.happy ||
      hasFact(Fact.Added) ||
      (hasFact(Fact.WasOrphan) && !(hasFact(Fact.Deleted) || hasLostNodeTag)) ||
      hasFact(Fact.WasIgnored)
  }

  private def investigate(): Boolean = {

    if (nodeChange.changeType == ChangeType.InitialValue) {
      return false
    }

    if (nodeChange.changeType == ChangeType.Delete) {
      return true
    }

    nodeChange.definedInNetworkChanges.exists(_.after == false) ||
      nodeChange.removedFromRoute.nonEmpty ||
      nodeChange.removedFromNetwork.nonEmpty ||
      nodeChange.factDiffs.investigate ||
      hasFact(Fact.Deleted) ||
      hasFact(Fact.BecomeIgnored) ||
      hasFact(Fact.BecomeOrphan) ||
      hasLostNodeTag
  }

  private def hasLostNodeTag: Boolean = {
    hasFact(Fact.LostHikingNodeTag) ||
      hasFact(Fact.LostBicycleNodeTag) ||
      hasFact(Fact.LostHorseNodeTag) ||
      hasFact(Fact.LostMotorboatNodeTag) ||
      hasFact(Fact.LostCanoeNodeTag) ||
      hasFact(Fact.LostInlineSkateNodeTag)
  }

  private def hasFact(fact: Fact): Boolean = {
    nodeChange.facts.contains(fact)
  }

}
