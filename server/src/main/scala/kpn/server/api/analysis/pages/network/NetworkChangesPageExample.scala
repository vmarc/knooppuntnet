package kpn.server.api.analysis.pages.network

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.network.NetworkChangesPage
import kpn.api.common.network.NetworkSummary

object NetworkChangesPageExample {

  val page: NetworkChangesPage = {

    val filterOptions: Seq[ChangesFilterOption] = Seq.empty
    val changes: Seq[NetworkChangeInfo] = Seq.empty

    NetworkChangesPage(
      NetworkSummary(
        Some("name"),
        RouteType.hiking,
        RouteScope.regional,
        1,
        2,
        3,
      ),
      filterOptions,
      changes,
      totalCount = 10
    )
  }
}
