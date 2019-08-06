import {TimeInfo} from "../../../../kpn/shared/time-info";
import {NetworkRouteFilterCriteria} from "./network-route-filter-criteria";
import {BooleanFilter} from "../../../../kpn/filter/boolean-filter";
import {NetworkRouteRow} from "../../../../kpn/shared/network/network-route-row";
import {Filters} from "../../../../kpn/filter/filters";
import {List} from "immutable";
import {FilterOptions} from "../../../../kpn/filter/filter-options";
import {BehaviorSubject} from "rxjs";

export class NetworkRouteFilter {

  constructor(readonly timeInfo: TimeInfo,
              readonly criteria: NetworkRouteFilterCriteria,
              readonly filterCriteria: BehaviorSubject<NetworkRouteFilterCriteria>) {
  }

  private investigateFilter: BooleanFilter<NetworkRouteRow> = new BooleanFilter<NetworkRouteRow>(
    "investigate",
    this.criteria.investigate,
    (row) => row.investigate,
    () => this.filterCriteria.next({...this.criteria, investigate: null}),
    () => this.filterCriteria.next({...this.criteria, investigate: true}),
    () => this.filterCriteria.next({...this.criteria, investigate: false})
  );

  private accessibleFilter: BooleanFilter<NetworkRouteRow> = new BooleanFilter<NetworkRouteRow>(
    "accessible",
    this.criteria.accessible,
    (row) => row.accessible,
    () => this.filterCriteria.next({...this.criteria, accessible: null}),
    () => this.filterCriteria.next({...this.criteria, accessible: true}),
    () => this.filterCriteria.next({...this.criteria, accessible: false})
  );

  private roleConnectionFilter: BooleanFilter<NetworkRouteRow> = new BooleanFilter<NetworkRouteRow>(
    "roleConnection",
    this.criteria.roleConnection,
    (row) => row.roleConnection,
    () => this.filterCriteria.next({...this.criteria, roleConnection: null}),
    () => this.filterCriteria.next({...this.criteria, roleConnection: true}),
    () => this.filterCriteria.next({...this.criteria, roleConnection: false})
  );

  // private lastUpdatedFilter: Filter<NetworkRouteRow> = null; //  = new TimestampFilter<NetworkRouteRow>();
  //   criteria.lastUpdated,
  //   _.relationLastUpdated,
  //   timeInfo,
  //   CallbackTo {
  //   updateCriteria(criteria.copy(lastUpdated = TimeFilterKind.ALL))
  // },
  // CallbackTo {
  //   updateCriteria(criteria.copy(lastUpdated = TimeFilterKind.LAST_WEEK))
  // },
  // CallbackTo {
  //   updateCriteria(criteria.copy(lastUpdated = TimeFilterKind.LAST_MONTH))
  // },
  // CallbackTo {
  //   updateCriteria(criteria.copy(lastUpdated = TimeFilterKind.LAST_YEAR))
  // },
  // CallbackTo {
  //   updateCriteria(criteria.copy(lastUpdated = TimeFilterKind.OLDER))
  // }
  // )

  private allFilters = new Filters<NetworkRouteRow>(
    List([
      this.investigateFilter,
      this.accessibleFilter,
      this.roleConnectionFilter
      // this.lastUpdatedFilter
    ])
  );

  filter(routes: List<NetworkRouteRow>): List<NetworkRouteRow> {
    return routes.filter(r => this.allFilters.passes(r));
  }

  filterOptions(routes: List<NetworkRouteRow>): FilterOptions {

    const totalCount = routes.size;
    const filteredCount = routes.count(r => this.allFilters.passes(r));

    const investigate = this.investigateFilter.filterOptions(this.allFilters, routes);
    const accessible = this.accessibleFilter.filterOptions(this.allFilters, routes);
    const roleConnection = this.roleConnectionFilter.filterOptions(this.allFilters, routes);
    // const lastUpdated = this.lastUpdatedFilter.filterOptions(this.allFilters, routes);

    const groups = List([
      investigate,
      accessible,
      roleConnection
      // lastUpdated
    ]).filter(g => g !== null);

    return new FilterOptions(
      filteredCount,
      totalCount,
      groups);
  }
}
