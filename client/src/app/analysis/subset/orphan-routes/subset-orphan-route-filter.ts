import {TimeInfo} from "../../../kpn/shared/time-info";
import {SubsetOrphanRouteFilterCriteria} from "./subset-orphan-route-filter-criteria";
import {BehaviorSubject} from "rxjs";
import {RouteSummary} from "../../../kpn/shared/route-summary";
import {BooleanFilter} from "../../../kpn/filter/boolean-filter";
import {TimestampFilter} from "../../../kpn/filter/timestamp-filter";
import {Filters} from "../../../kpn/filter/filters";
import {TimestampFilterKind} from "../../../kpn/filter/timestamp-filter-kind";
import {List} from "immutable";
import {FilterOptions} from "../../../kpn/filter/filter-options";

export class SubsetOrphanRouteFilter {

  constructor(private readonly timeInfo: TimeInfo,
              private readonly criteria: SubsetOrphanRouteFilterCriteria,
              private readonly filterCriteria: BehaviorSubject<SubsetOrphanRouteFilterCriteria>) {
  }

  private readonly definedInNetworkRelationFilter = new BooleanFilter<RouteSummary>(
    "definedInNetworkRelation",
    this.criteria.broken,
    (row) => row.isBroken,
    this.update({...this.criteria, broken: null}),
    this.update({...this.criteria, broken: true}),
    this.update({...this.criteria, broken: false})
  );

  private readonly lastUpdatedFilter = new TimestampFilter<RouteSummary>(
    this.criteria.lastUpdated,
    (row) => row.timestamp,
    this.timeInfo,
    this.update({...this.criteria, lastUpdated: TimestampFilterKind.ALL}),
    this.update({...this.criteria, lastUpdated: TimestampFilterKind.LAST_WEEK}),
    this.update({...this.criteria, lastUpdated: TimestampFilterKind.LAST_MONTH}),
    this.update({...this.criteria, lastUpdated: TimestampFilterKind.LAST_YEAR}),
    this.update({...this.criteria, lastUpdated: TimestampFilterKind.OLDER})
  );

  private readonly allFilters = new Filters<RouteSummary>(
    // TODO this.definedInNetworkRelationFilter, ??
    this.lastUpdatedFilter
  );

  filter(routes: List<RouteSummary>): List<RouteSummary> {
    return routes.filter(route => this.allFilters.passes(route));
  }

  filterOptions(routes: List<RouteSummary>): FilterOptions {

    const totalCount = routes.size;
    const filteredCount = routes.count(this.allFilters.passes);

    // TODO this.definedInNetworkRelationFilter ??
    const lastUpdated = this.lastUpdatedFilter.filterOptions(this.allFilters, routes);

    const groups = List([
      lastUpdated
    ]).filter(g => g !== null);

    return new FilterOptions(
      filteredCount,
      totalCount,
      groups
    );
  }

  private update(criteria: SubsetOrphanRouteFilterCriteria) {
    return () => this.filterCriteria.next(criteria);
  }

}
