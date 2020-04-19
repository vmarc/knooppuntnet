package kpn.server.api.analysis.pages.network

import kpn.api.common.NetworkFacts
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.network.NetworkChangesPage
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkInfoDetail
import kpn.api.common.network.NetworkShape
import kpn.api.custom.Tags

object NetworkChangesPageExample {

  val page: NetworkChangesPage = {

    val networkInfo: NetworkInfo = NetworkInfo(
      attributes = NetworkDetailsPageExample.networkAttributes(),
      active = false,
      nodeRefs = Seq(),
      routeRefs = Seq(),
      networkRefs = Seq(),
      facts = Seq(),
      tags = Tags.empty,
      detail = Some(
        NetworkInfoDetail(
          nodes = Seq.empty,
          routes = Seq.empty,
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
