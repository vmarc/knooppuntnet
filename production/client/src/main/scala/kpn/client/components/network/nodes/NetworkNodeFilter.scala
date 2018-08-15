package kpn.client.components.network.nodes

import japgolly.scalajs.react.CallbackTo
import kpn.client.filter.BooleanFilter
import kpn.client.filter.FilterOption
import kpn.client.filter.FilterOptionGroup
import kpn.client.filter.FilterOptions
import kpn.client.filter.Filters
import kpn.client.filter.TimeFilterKind
import kpn.client.filter.TimestampFilter
import kpn.shared.TimeInfo
import kpn.shared.network.NetworkNodeInfo2

class NetworkNodeFilter(timeInfo: TimeInfo, criteria: NetworkNodeFilterCriteria, updateCriteria: (NetworkNodeFilterCriteria) => Unit) {

  private val definedInNetworkRelationFilter = new BooleanFilter[NetworkNodeInfo2](
    "definedInNetworkRelation",
    criteria.definedInNetworkRelation,
    _.definedInRelation,
    CallbackTo {
      updateCriteria(criteria.copy(definedInNetworkRelation = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(definedInNetworkRelation = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(definedInNetworkRelation = Some(false)))
    }
  )

  private val definedInRouteRelationFilter = new BooleanFilter[NetworkNodeInfo2](
    "definedInRouteRelation",
    criteria.definedInRouteRelation,
    _.definedInRoute,
    CallbackTo {
      updateCriteria(criteria.copy(definedInRouteRelation = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(definedInRouteRelation = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(definedInRouteRelation = Some(false)))
    }
  )

  private val referencedInRouteFilter = new BooleanFilter[NetworkNodeInfo2](
    "referencedInRoute",
    criteria.referencedInRoute,
    _.routeReferences.nonEmpty,
    CallbackTo {
      updateCriteria(criteria.copy(referencedInRoute = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(referencedInRoute = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(referencedInRoute = Some(false)))
    }
  )

  private val connectionFilter = new BooleanFilter[NetworkNodeInfo2](
    "connection",
    criteria.connection,
    _.connection,
    CallbackTo {
      updateCriteria(criteria.copy(connection = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(connection = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(connection = Some(false)))
    }
  )

  private val integrityCheckFilter = new BooleanFilter[NetworkNodeInfo2](
    "integrityCheck",
    criteria.integrityCheck,
    _.integrityCheck.isDefined,
    CallbackTo {
      updateCriteria(criteria.copy(integrityCheck = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(integrityCheck = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(integrityCheck = Some(false)))
    }
  )

  private val integrityCheckFailedFilter = new BooleanFilter[NetworkNodeInfo2](
    "integrityCheckFailed",
    criteria.integrityCheckFailed,
    _.integrityCheck match {
      case Some(e) => !e.failed
      case None => false
    },
    CallbackTo {
      updateCriteria(criteria.copy(integrityCheckFailed = None))
    },
    CallbackTo {
      updateCriteria(criteria.copy(integrityCheckFailed = Some(true)))
    },
    CallbackTo {
      updateCriteria(criteria.copy(integrityCheckFailed = Some(false)))
    }
  )

  private val lastUpdatedFilter = new TimestampFilter[NetworkNodeInfo2](
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

  private val allFilters = new Filters[NetworkNodeInfo2](
    definedInNetworkRelationFilter,
    definedInRouteRelationFilter,
    referencedInRouteFilter,
    connectionFilter,
    integrityCheckFilter,
    integrityCheckFailedFilter,
    lastUpdatedFilter
  )

  def filter(nodes: Seq[NetworkNodeInfo2]): Seq[NetworkNodeInfo2] = {
    nodes.filter(allFilters.passes)
  }

  def filterOptions(nodes: Seq[NetworkNodeInfo2]): FilterOptions = {

    val totalCount = nodes.size
    val filteredCount = nodes.count(allFilters.passes)

    val definedInNetworkRelation = definedInNetworkRelationFilter.filterOptions(allFilters, nodes)
    val definedInRouteRelation = definedInRouteRelationFilter.filterOptions(allFilters, nodes)
    val referencedInRoute = referencedInRouteFilter.filterOptions(allFilters, nodes)
    val connection = connectionFilter.filterOptions(allFilters, nodes)
    val integrityCheck = integrityCheckFilter.filterOptions(allFilters, nodes)

    val integrityCheckResult = {
      // TODO move into separate class ???

      val filteredElements = allFilters.filterExcept(nodes, integrityCheckFailedFilter).filter(_.integrityCheck.isDefined)
      val (yesElements, noElements) = filteredElements.partition(integrityCheckFailedFilter.booleanPropertyAccessor)
      val active = filteredElements.nonEmpty && yesElements.nonEmpty && noElements.nonEmpty

      if (active) {
        val all = FilterOption(
          "all",
          filteredElements.size,
          criteria.integrityCheckFailed.isEmpty,
          CallbackTo {
            updateCriteria(criteria.copy(integrityCheckFailed = None))
          }
        )

        val yes = FilterOption(
          "yes",
          yesElements.size,
          criteria.integrityCheckFailed.contains(true),
          CallbackTo {
            updateCriteria(criteria.copy(integrityCheckFailed = Some(true)))
          }
        )

        val no = FilterOption(
          "no",
          noElements.size,
          criteria.integrityCheckFailed.contains(false),
          CallbackTo {
            updateCriteria(criteria.copy(integrityCheckFailed = Some(false)))
          }
        )
        Some(FilterOptionGroup(integrityCheckFailedFilter.name, all, yes, no))
      }
      else {
        None
      }
    }

    val lastUpdated = lastUpdatedFilter.filterOptions(allFilters, nodes)

    val groups = Seq(
      definedInNetworkRelation,
      definedInRouteRelation,
      referencedInRoute,
      connection,
      integrityCheck,
      integrityCheckResult,
      lastUpdated
    ).flatten

    FilterOptions(
      filteredCount,
      totalCount,
      groups: _*
    )
  }
}
