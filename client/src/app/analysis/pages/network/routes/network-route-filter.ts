import {TimeInfo} from "../../../../kpn/shared/time-info";
import {NetworkRouteFilterCriteria} from "./network-route-filter-criteria";
import {BooleanFilter} from "../../../../kpn/filter/boolean-filter";
import {NetworkRouteRow} from "../../../../kpn/shared/network/network-route-row";
import {Filters} from "../../../../kpn/filter/filters";
import {List} from "immutable";
import {FilterOptions} from "../../../../kpn/filter/filter-options";
import {BehaviorSubject} from "rxjs";
import {TimestampFilter} from "../../../../kpn/filter/timestamp-filter";
import {TimestampFilterKind} from "../../../../kpn/filter/timestamp-filter-kind";

export class NetworkRouteFilter {

  constructor(private timeInfo: TimeInfo,
              private criteria: NetworkRouteFilterCriteria,
              private filterCriteria: BehaviorSubject<NetworkRouteFilterCriteria>) {
  }

  private readonly investigateFilter = new BooleanFilter<NetworkRouteRow>(
    "investigate",
    this.criteria.investigate,
    (row) => row.investigate,
    this.update({...this.criteria, investigate: null}),
    this.update({...this.criteria, investigate: true}),
    this.update({...this.criteria, investigate: false})
  );

  private readonly accessibleFilter = new BooleanFilter<NetworkRouteRow>(
    "accessible",
    this.criteria.accessible,
    (row) => row.accessible,
    this.update({...this.criteria, accessible: null}),
    this.update({...this.criteria, accessible: true}),
    this.update({...this.criteria, accessible: false})
  );

  private readonly roleConnectionFilter = new BooleanFilter<NetworkRouteRow>(
    "roleConnection",
    this.criteria.roleConnection,
    (row) => row.roleConnection,
    this.update({...this.criteria, roleConnection: null}),
    this.update({...this.criteria, roleConnection: true}),
    this.update({...this.criteria, roleConnection: false})
  );

  private readonly lastUpdatedFilter = new TimestampFilter<NetworkRouteRow>(
    this.criteria.relationLastUpdated,
    (row) => row.relationLastUpdated,
    this.timeInfo,
    this.update({...this.criteria, relationLastUpdated: TimestampFilterKind.ALL}),
    this.update({...this.criteria, relationLastUpdated: TimestampFilterKind.LAST_WEEK}),
    this.update({...this.criteria, relationLastUpdated: TimestampFilterKind.LAST_MONTH}),
    this.update({...this.criteria, relationLastUpdated: TimestampFilterKind.LAST_YEAR}),
    this.update({...this.criteria, relationLastUpdated: TimestampFilterKind.OLDER})
  );

  private readonly allFilters = new Filters<NetworkRouteRow>(
    this.investigateFilter,
    this.accessibleFilter,
    this.roleConnectionFilter,
    this.lastUpdatedFilter
  );

  filter(routes: List<NetworkRouteRow>): List<NetworkRouteRow> {
    return routes.filter(r => this.allFilters.passes(r));
  }

  filterOptions(routes: List<NetworkRouteRow>): FilterOptions {

    const totalCount = routes.size;
    const filteredCount = routes.count(route => this.allFilters.passes(route));

    const investigate = this.investigateFilter.filterOptions(this.allFilters, routes);
    const accessible = this.accessibleFilter.filterOptions(this.allFilters, routes);
    const roleConnection = this.roleConnectionFilter.filterOptions(this.allFilters, routes);
    const lastUpdated = this.lastUpdatedFilter.filterOptions(this.allFilters, routes);

    const groups = List([
      investigate,
      accessible,
      roleConnection,
      lastUpdated
    ]).filter(g => g !== null);

    return new FilterOptions(
      filteredCount,
      totalCount,
      groups);
  }

  private update(criteria: NetworkRouteFilterCriteria) {
    return () => this.filterCriteria.next(criteria);
  }

}
