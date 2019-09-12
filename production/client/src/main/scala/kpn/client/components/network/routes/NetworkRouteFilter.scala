// Migrated to Angular: network-route-filter.ts
package kpn.client.components.network.routes

import japgolly.scalajs.react.CallbackTo
import kpn.client.filter.BooleanFilter
import kpn.client.filter.FilterOptions
import kpn.client.filter.Filters
import kpn.client.filter.TimeFilterKind
import kpn.client.filter.TimestampFilter
import kpn.shared.TimeInfo
import kpn.shared.network.NetworkRouteRow

class NetworkRouteFilter(timeInfo: TimeInfo, criteria: NetworkRouteFilterCriteria, updateCriteria: NetworkRouteFilterCriteria => Unit) {

  private val taggedFilter = new BooleanFilter[NetworkRouteRow](
    "tagged",
    criteria.tagged,
    _.tagged,
    CallbackTo {
      updateCriteria(criteria.copy(tagged = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(tagged = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(tagged = Some(false)))
    }
  )

  private val investigateFilter = new BooleanFilter[NetworkRouteRow](
    "investigate",
    criteria.investigate,
    _.investigate,
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

  private val accessibleFilter = new BooleanFilter[NetworkRouteRow](
    "accessible",
    criteria.accessible,
    _.accessible,
    CallbackTo {
      updateCriteria(criteria.copy(accessible = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(accessible = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(accessible = Some(false)))
    }
  )

  private val roleConnectionFilter = new BooleanFilter[NetworkRouteRow](
    "roleConnection",
    criteria.roleConnection,
    _.roleConnection,
    CallbackTo {
      updateCriteria(criteria.copy(roleConnection = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(roleConnection = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(roleConnection = Some(false)))
    }
  )

  private val lastUpdatedFilter = new TimestampFilter[NetworkRouteRow](
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

  private val allFilters = new Filters[NetworkRouteRow](
    taggedFilter,
    investigateFilter,
    accessibleFilter,
    roleConnectionFilter,
    lastUpdatedFilter
  )

  def filter(nodes: Seq[NetworkRouteRow]): Seq[NetworkRouteRow] = {
    nodes.filter(allFilters.passes)
  }

  def filterOptions(routes: Seq[NetworkRouteRow]): FilterOptions = {

    val totalCount = routes.size
    val filteredCount = routes.count(allFilters.passes)

    val tagged = taggedFilter.filterOptions(allFilters, routes)
    val investigate = investigateFilter.filterOptions(allFilters, routes)
    val accessible = accessibleFilter.filterOptions(allFilters, routes)
    val roleConnection = roleConnectionFilter.filterOptions(allFilters, routes)
    val lastUpdated = lastUpdatedFilter.filterOptions(allFilters, routes)

    val groups = Seq(
      tagged,
      investigate,
      accessible,
      roleConnection,
      lastUpdated
    ).flatten

    FilterOptions(
      filteredCount,
      totalCount,
      groups: _*
    )
  }
}
