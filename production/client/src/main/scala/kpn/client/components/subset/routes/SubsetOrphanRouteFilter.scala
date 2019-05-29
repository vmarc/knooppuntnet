// TODO migrate to Angular
package kpn.client.components.subset.routes

import japgolly.scalajs.react.CallbackTo
import kpn.client.filter.BooleanFilter
import kpn.client.filter.FilterOptions
import kpn.client.filter.Filters
import kpn.client.filter.TimeFilterKind
import kpn.client.filter.TimestampFilter
import kpn.shared.RouteSummary
import kpn.shared.TimeInfo

class SubsetOrphanRouteFilter(timeInfo: TimeInfo, criteria: SubsetOrphanRouteFilterCriteria, updateCriteria: (SubsetOrphanRouteFilterCriteria) => Unit) {

  private val definedInNetworkRelationFilter = new BooleanFilter[RouteSummary](
    "definedInNetworkRelation",
    criteria.broken,
    _.isBroken,
    CallbackTo {
      updateCriteria(criteria.copy(broken = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(broken = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(broken = Some(false)))
    }
  )

  private val lastUpdatedFilter = new TimestampFilter[RouteSummary](
    criteria.lastUpdated,
    _.timestamp,
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

  private val allFilters = new Filters[RouteSummary](
    lastUpdatedFilter
  )

  def filter(routes: Seq[RouteSummary]): Seq[RouteSummary] = {
    routes.filter(allFilters.passes)
  }

  def filterOptions(routes: Seq[RouteSummary]): FilterOptions = {

    val totalCount = routes.size
    val filteredCount = routes.count(allFilters.passes)

    val lastUpdated = lastUpdatedFilter.filterOptions(allFilters, routes)

    val groups = Seq(
      lastUpdated
    ).flatten

    FilterOptions(
      filteredCount,
      totalCount,
      groups: _*
    )
  }
}
