package kpn.core.tools.route

import kpn.api.common.route.RouteInfo

class RouteFactsComparator {

  def compare(oldRoute: RouteInfo, newRoute: RouteInfo): Option[RouteFactDiff] = {
    if (oldRoute.facts.toSet == newRoute.facts.toSet) {
      None
    }
    else {
      Some(
        RouteFactDiff(oldRoute.facts.toSet, newRoute.facts.toSet)
      )
    }
  }

}
