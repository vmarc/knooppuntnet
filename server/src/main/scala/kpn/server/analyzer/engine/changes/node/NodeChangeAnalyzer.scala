package kpn.server.analyzer.engine.changes.node

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.custom.Fact

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
      (hasFact(Fact.WasOrphan) && !(hasFact(Fact.Deleted) || hasLostNodeTag))
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
