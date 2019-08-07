import {TimeInfo} from "../../../../kpn/shared/time-info";
import {NetworkRouteFilterCriteria} from "./network-route-filter-criteria";
import {BooleanFilter} from "../../../../kpn/filter/boolean-filter";
import {NetworkRouteRow} from "../../../../kpn/shared/network/network-route-row";
import {Filters} from "../../../../kpn/filter/filters";
import {List} from "immutable";
import {FilterOptions} from "../../../../kpn/filter/filter-options";
import {BehaviorSubject} from "rxjs";

export class NetworkRouteFilter {

  constructor(private timeInfo: TimeInfo,
              private criteria: NetworkRouteFilterCriteria,
              private filterCriteria: BehaviorSubject<NetworkRouteFilterCriteria>) {
  }

  private investigateFilter = new BooleanFilter<NetworkRouteRow>(
    "investigate",
    this.criteria.investigate,
    (row) => row.investigate,
    this.update({...this.criteria, investigate: null}),
    this.update({...this.criteria, investigate: true}),
    this.update({...this.criteria, investigate: false})
  );

  private accessibleFilter = new BooleanFilter<NetworkRouteRow>(
    "accessible",
    this.criteria.accessible,
    (row) => row.accessible,
    this.update({...this.criteria, accessible: null}),
    this.update({...this.criteria, accessible: true}),
    this.update({...this.criteria, accessible: false})
  );

  private roleConnectionFilter = new BooleanFilter<NetworkRouteRow>(
    "roleConnection",
    this.criteria.roleConnection,
    (row) => row.roleConnection,
    this.update({...this.criteria, roleConnection: null}),
    this.update({...this.criteria, roleConnection: true}),
    this.update({...this.criteria, roleConnection: false})
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
    this.investigateFilter,
    this.accessibleFilter,
    this.roleConnectionFilter
    // this.lastUpdatedFilter
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

  private update(criteria: NetworkRouteFilterCriteria) {
    return () => this.filterCriteria.next(criteria);
  }

}
