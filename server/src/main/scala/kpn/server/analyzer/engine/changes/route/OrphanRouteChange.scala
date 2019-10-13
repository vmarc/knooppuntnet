package kpn.server.analyzer.engine.changes.route

import kpn.shared.Fact
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.RouteChange

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
