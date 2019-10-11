package kpn.server.api.analysis.pages.route

import kpn.shared.route.RouteChangeInfos
import kpn.shared.route.RoutePage

object RoutePageExample {

  val page: RoutePage = {
    val newPage = RouteDetailsPageExample.page
    RoutePage(
      newPage.route,
      newPage.references,
      RouteChangeInfos(
        RouteChangesPageExample.changes(),
        incompleteWarning = true
      )
    )
  }

}
