package kpn.shared.route

case class RoutePage(
  route: RouteInfo,
  references: RouteReferences,
  routeChangeInfos: RouteChangeInfos
) {

  def title: String = {
    val networkName = if (references.networkReferences.isEmpty) {
      ""
    }
    else {
      " - " + references.networkReferences.head.name
    }
    route.summary.name + networkName
  }
}
