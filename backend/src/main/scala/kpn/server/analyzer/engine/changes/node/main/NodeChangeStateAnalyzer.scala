package kpn.server.analyzer.engine.changes.node.main

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.changes.details.NodeChange
import kpn.core.analysis.Facts

object NodeChangeStateAnalyzer {
  def analyzed(nodeChange: NodeChange): NodeChange = {
    new NodeChangeStateAnalyzer(nodeChange).analyzed()
  }
}

class NodeChangeStateAnalyzer(nodeChange: NodeChange) {

  def analyzed(): NodeChange = {
    val happy = determineHappy()
    val investigate = determineInvestigate()
    val locationHappy = determineLocationHappy()
    val locationInvestigate = determineLocationInvestigate()
    nodeChange.copy(
      happy = happy,
      investigate = investigate,
      impact = happy || investigate,
      locationHappy = locationHappy,
      locationInvestigate = locationInvestigate,
      locationImpact = locationHappy || locationInvestigate
    )
  }

  private def determineHappy(): Boolean = {

    if (nodeChange.changeType == ChangeType.InitialValue) {
      return false
    }

    if (nodeChange.changeType == ChangeType.Create) {
      return true
    }

    nodeChange.definedInNetworkChanges.exists(_.after) ||
      nodeChange.addedToRoute.nonEmpty ||
      nodeChange.addedToNetwork.nonEmpty ||
      nodeChange.factDiffs.exists(_.happy) ||
      hasFact(Fact.Added)
  }

  private def determineLocationHappy(): Boolean = {

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

    if (nodeChange.factDiffs.exists(_.resolved.exists(Facts.locationFacts.contains))) {
      return true
    }

    false
  }

  private def determineInvestigate(): Boolean = {

    if (nodeChange.changeType == ChangeType.InitialValue) {
      return false
    }

    if (nodeChange.changeType == ChangeType.Delete) {
      return true
    }

    nodeChange.definedInNetworkChanges.exists(_.after == false) ||
      nodeChange.removedFromRoute.nonEmpty ||
      nodeChange.removedFromNetwork.nonEmpty ||
      nodeChange.factDiffs.exists(_.investigate) ||
      hasFact(Fact.Deleted) ||
      hasLostNodeTag
  }

  private def determineLocationInvestigate(): Boolean = {

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

    if (nodeChange.factDiffs.exists(_.introduced.filter(Facts.isError).exists(Facts.locationFacts.contains))) {
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
