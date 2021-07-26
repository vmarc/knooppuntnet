package kpn.server.analyzer.engine.changes.route

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.custom.Fact

object RouteChangeStateAnalyzer {
  def analyzed(routeChange: RouteChange): RouteChange = {
    new RouteChangeStateAnalyzer(routeChange).analyzed()
  }
}

class RouteChangeStateAnalyzer(routeChange: RouteChange) {

  def analyzed(): RouteChange = {

    val happy = determineHappy()
    val investigate = determineInvestigate()
    val locationHappy = determineLocationHappy()
    val locationInvestigate = determineLocationInvestigate()

    routeChange.copy(
      happy = happy,
      investigate = investigate,
      impact = happy || investigate,
      locationHappy = locationHappy,
      locationInvestigate = locationInvestigate,
      locationImpact = locationHappy || locationInvestigate
    )
  }

  private def determineHappy(): Boolean = {

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

  private def determineLocationHappy(): Boolean = {

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

  private def determineInvestigate(): Boolean = {

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

  private def determineLocationInvestigate(): Boolean = {

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
