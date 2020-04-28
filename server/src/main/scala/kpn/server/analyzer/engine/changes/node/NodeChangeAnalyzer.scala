package kpn.server.analyzer.engine.changes.node

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.custom.Fact

object NodeChangeAnalyzer {
  def analyzed(nodeChange: NodeChange): NodeChange = {
    new NodeChangeAnalyzer(nodeChange).analyzed()
  }
}

class NodeChangeAnalyzer(nodeChange: NodeChange) {

  def analyzed(): NodeChange = {
    nodeChange.copy(
      happy = happy(),
      investigate = investigate(),
      locationHappy = locationHappy(),
      locationInvestigate = locationInvestigate()
    )
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

  private def locationHappy(): Boolean = {

    if (nodeChange.changeType == ChangeType.InitialValue) {
      return false
    }

    if (hasFact(Fact.Deleted) || hasLostNodeTag) {
      return false
    }

    if (nodeChange.changeType == ChangeType.Create) {
      return true
    }

    if (nodeChange.addedToRoute.nonEmpty) {
      return true
    }

    if (hasFact(Fact.Added)) {
      return true
    }

    if (nodeChange.factDiffs.resolved.exists(Fact.locationFacts.contains)) {
      return true
    }

    false
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

  private def locationInvestigate(): Boolean = {

    if (nodeChange.changeType == ChangeType.InitialValue) {
      return false
    }

    if (nodeChange.changeType == ChangeType.Delete) {
      return true
    }

    if (nodeChange.removedFromRoute.nonEmpty) {
      return true
    }

    if (hasFact(Fact.Deleted) || hasLostNodeTag) {
      return true
    }

    if (nodeChange.factDiffs.introduced.filter(_.isError).exists(Fact.locationFacts.contains)) {
      return true
    }

    false
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
