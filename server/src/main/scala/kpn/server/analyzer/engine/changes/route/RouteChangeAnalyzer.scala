package kpn.server.analyzer.engine.changes.route

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.custom.Fact

object RouteChangeAnalyzer {
  def analyzed(routeChange: RouteChange): RouteChange = {
    new RouteChangeAnalyzer(routeChange).analyzed()
  }
}

class RouteChangeAnalyzer(routeChange: RouteChange) {

  def analyzed(): RouteChange = {
    routeChange.copy(
      happy = happy(),
      investigate = investigate(),
      locationHappy = locationHappy(),
      locationInvestigate = locationInvestigate()
    )
  }

  private def happy(): Boolean = {

    if (routeChange.changeType == ChangeType.InitialValue) {
      return false
    }

    if (routeChange.changeType == ChangeType.Create) {
      return true
    }

    routeChange.addedToNetwork.nonEmpty ||
      routeChange.diffs.happy ||
      hasFact(Fact.Added) ||
      (hasFact(Fact.WasOrphan) && !(hasFact(Fact.Deleted) || hasFact(Fact.LostRouteTags)))
  }

  private def locationHappy(): Boolean = {

    if (routeChange.changeType == ChangeType.InitialValue) {
      return false
    }

    if (routeChange.changeType == ChangeType.Create) {
      return true
    }

    routeChange.addedToNetwork.nonEmpty ||
      routeChange.diffs.happy ||
      hasFact(Fact.Added) ||
      (hasFact(Fact.WasOrphan) && !(hasFact(Fact.Deleted) || hasFact(Fact.LostRouteTags)))
  }

  private def investigate(): Boolean = {

    if (routeChange.changeType == ChangeType.InitialValue) {
      return false
    }

    if (routeChange.changeType == ChangeType.Delete) {
      return true
    }

    routeChange.removedFromNetwork.nonEmpty ||
      routeChange.diffs.investigate ||
      hasFact(Fact.Deleted) ||
      hasFact(Fact.BecomeOrphan) ||
      hasFact(Fact.LostRouteTags)
  }

  private def locationInvestigate(): Boolean = {

    if (routeChange.changeType == ChangeType.InitialValue) {
      return false
    }

    if (routeChange.changeType == ChangeType.Delete) {
      return true
    }

    if (hasFact(Fact.Deleted) || hasFact(Fact.LostRouteTags)) {
      return true
    }

    routeChange.diffs.factDiffs match {
      case Some(factDiffs) =>
        factDiffs.introduced.filter(_.isError).exists(Fact.locationFacts.contains)
      case None => false
    }
  }

  private def hasFact(fact: Fact): Boolean = {
    routeChange.facts.contains(fact)
  }
}
