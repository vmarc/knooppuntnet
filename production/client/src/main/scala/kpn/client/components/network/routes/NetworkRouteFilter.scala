package kpn.client.components.network.routes

import japgolly.scalajs.react.CallbackTo
import kpn.client.filter.BooleanFilter
import kpn.client.filter.FilterOptions
import kpn.client.filter.Filters
import kpn.client.filter.StringFilter
import kpn.client.filter.TimeFilterKind
import kpn.client.filter.TimestampFilter
import kpn.shared.Fact
import kpn.shared.TimeInfo
import kpn.shared.network.NetworkRouteInfo

class NetworkRouteFilter(timeInfo: TimeInfo, criteria: NetworkRouteFilterCriteria, updateCriteria: (NetworkRouteFilterCriteria) => Unit) {

  private val investigateFilter = new BooleanFilter[NetworkRouteInfo](
    "investigate",
    criteria.investigate,
    _.facts.contains(Fact.RouteBroken),
    CallbackTo {
      updateCriteria(criteria.copy(investigate = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(investigate = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(investigate = Some(false)))
    }
  )

  private val unaccessibleFilter = new BooleanFilter[NetworkRouteInfo](
    "unaccessible",
    criteria.unaccessible,
    _.facts.contains(Fact.RouteUnaccessible),
    CallbackTo {
      updateCriteria(criteria.copy(unaccessible = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(unaccessible = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(unaccessible = Some(false)))
    }
  )

  private def updateRoleCriteria(strings: Seq[String]): Unit = {
    updateCriteria(criteria.copy(role = strings))
  }

  private val roleFilter = new StringFilter[NetworkRouteInfo](
    "role",
    criteria.role,
    (r) => r.role,
    updateRoleCriteria
  )

  private val lastUpdatedFilter = new TimestampFilter[NetworkRouteInfo](
    criteria.lastUpdated,
    _.relationLastUpdated,
    timeInfo,
    CallbackTo {
      updateCriteria(criteria.copy(lastUpdated = TimeFilterKind.ALL))
    },
    CallbackTo {
      updateCriteria(criteria.copy(lastUpdated = TimeFilterKind.LAST_WEEK))
    },
    CallbackTo {
      updateCriteria(criteria.copy(lastUpdated = TimeFilterKind.LAST_MONTH))
    },
    CallbackTo {
      updateCriteria(criteria.copy(lastUpdated = TimeFilterKind.LAST_YEAR))
    },
    CallbackTo {
      updateCriteria(criteria.copy(lastUpdated = TimeFilterKind.OLDER))
    }
  )

  private val allFilters = new Filters[NetworkRouteInfo](
    investigateFilter,
    unaccessibleFilter,
    roleFilter,
    lastUpdatedFilter
  )

  def filter(nodes: Seq[NetworkRouteInfo]): Seq[NetworkRouteInfo] = {
    nodes.filter(allFilters.passes)
  }

  def filterOptions(routes: Seq[NetworkRouteInfo]): FilterOptions = {

    val totalCount = routes.size
    val filteredCount = routes.count(allFilters.passes)

    val investigate = investigateFilter.filterOptions(allFilters, routes)
    val unaccessible = unaccessibleFilter.filterOptions(allFilters, routes)
    val role = roleFilter.filterOptions(allFilters, routes)

    val lastUpdated = lastUpdatedFilter.filterOptions(allFilters, routes)

    val groups = Seq(
      investigate,
      unaccessible,
      role,
      lastUpdated
    ).flatten

    FilterOptions(
      filteredCount,
      totalCount,
      groups: _*
    )
  }
}
