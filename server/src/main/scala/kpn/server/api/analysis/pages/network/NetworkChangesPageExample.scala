package kpn.server.api.analysis.pages.network

import kpn.api.common.NetworkFacts
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.network.NetworkChangesPage
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkInfoDetail
import kpn.api.common.network.NetworkShape
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags

object NetworkChangesPageExample {

  val page: NetworkChangesPage = {

    val networkInfo: NetworkInfo = NetworkInfo(
      1L,
      attributes = NetworkDetailsPageExample.networkAttributes(),
      active = false,
      nodeRefs = Seq.empty,
      routeRefs = Seq.empty,
      networkRefs = Seq.empty,
      facts = Seq.empty,
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

    val filterOptions: Seq[ChangesFilterOption] = Seq.empty
    val changes: Seq[NetworkChangeInfo] = Seq.empty

    NetworkChangesPage(
      NetworkSummary(
        "name",
        NetworkType.hiking,
        NetworkScope.regional,
        1,
        2,
        3,
        4
      ),
      filterOptions,
      changes,
      totalCount = 10
    )
  }
}
