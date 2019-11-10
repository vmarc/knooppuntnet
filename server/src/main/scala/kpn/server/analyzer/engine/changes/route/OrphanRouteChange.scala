package kpn.server.analyzer.engine.changes.route

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.custom.Fact

object OrphanRouteChange {

  def isOrphanRouteChange(routeChange: RouteChange): Boolean = {
    val facts = routeChange.facts
    routeChange.changeType match {
      case ChangeType.Create =>
        facts.contains(Fact.OrphanRoute)
      case ChangeType.Update =>
        facts.contains(Fact.OrphanRoute) || (facts.contains(Fact.WasOrphan) && facts.contains(Fact.LostRouteTags))
      case ChangeType.Delete =>
        facts.contains(Fact.OrphanRoute) || facts.contains(Fact.WasOrphan)
      case _ =>
        false
    }
  }

}
