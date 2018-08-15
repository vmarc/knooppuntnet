package kpn.client.components.subset.nodes

import japgolly.scalajs.react.CallbackTo
import kpn.client.filter.FilterOptions
import kpn.client.filter.Filters
import kpn.client.filter.TimeFilterKind
import kpn.client.filter.TimestampFilter
import kpn.shared.NodeInfo
import kpn.shared.TimeInfo

class SubsetOrphanNodeFilter(timeInfo: TimeInfo, criteria: SubsetOrphanNodeFilterCriteria, updateCriteria: (SubsetOrphanNodeFilterCriteria) => Unit) {

  private val lastUpdatedFilter = new TimestampFilter[NodeInfo](
    criteria.lastUpdated,
    _.lastUpdated,
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

  private val allFilters = new Filters[NodeInfo](
    lastUpdatedFilter
  )

  def filter(nodes: Seq[NodeInfo]): Seq[NodeInfo] = {
    nodes.filter(allFilters.passes)
  }

  def filterOptions(nodes: Seq[NodeInfo]): FilterOptions = {

    val totalCount = nodes.size
    val filteredCount = nodes.count(allFilters.passes)

    val lastUpdated = lastUpdatedFilter.filterOptions(allFilters, nodes)

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
