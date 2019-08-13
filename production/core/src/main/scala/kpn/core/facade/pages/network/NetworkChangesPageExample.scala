package kpn.core.facade.pages.network

import kpn.shared.NetworkFacts
import kpn.shared.changes.details.NetworkChangeInfo
import kpn.shared.changes.filter.ChangesFilter
import kpn.shared.data.Tags
import kpn.shared.network.NetworkChangesPage
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkInfoDetail
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.network.NetworkRouteInfo
import kpn.shared.network.NetworkShape

object NetworkChangesPageExample {

  val page: NetworkChangesPage = {

    val networkInfo: NetworkInfo = NetworkInfo(
      attributes = NetworkDetailsPageExample.networkAttributes(),
      active = false,
      ignored = true,
      nodeRefs = Seq(),
      routeRefs = Seq(),
      networkRefs = Seq(),
      facts = Seq(),
      tags = Tags.empty,
      detail = Some(
        NetworkInfoDetail(
          nodes = Seq[NetworkNodeInfo2](),
          routes = Seq[NetworkRouteInfo](),
          networkFacts = NetworkFacts(),
          shape = Some(NetworkShape())
        )
      )
    )

    val filter: ChangesFilter = ChangesFilter(Seq())
    val changes: Seq[NetworkChangeInfo] = Seq()

    NetworkChangesPage(
      NetworkSummaryBuilder.toSummary(networkInfo, 123),
      filter,
      changes,
      totalCount = 10
    )
  }
}
