import { OrphanRouteInfo } from '@api/common/orphan-route-info';
import { TimeInfo } from '@api/common/time-info';
import { List } from 'immutable';
import { BehaviorSubject } from 'rxjs';
import { BooleanFilter } from '../../../kpn/filter/boolean-filter';
import { FilterOptions } from '../../../kpn/filter/filter-options';
import { Filters } from '../../../kpn/filter/filters';
import { TimestampFilter } from '../../../kpn/filter/timestamp-filter';
import { TimestampFilterKind } from '../../../kpn/filter/timestamp-filter-kind';
import { SubsetOrphanRouteFilterCriteria } from './subset-orphan-route-filter-criteria';

export class SubsetOrphanRouteFilter {
  private readonly brokenFilter = new BooleanFilter<OrphanRouteInfo>(
    'broken',
    this.criteria.broken,
    (row) => row.isBroken,
    this.update({ ...this.criteria, broken: null }),
    this.update({ ...this.criteria, broken: true }),
    this.update({ ...this.criteria, broken: false })
  );
  private readonly lastUpdatedFilter = new TimestampFilter<OrphanRouteInfo>(
    this.criteria.lastUpdated,
    (row) => row.lastUpdated,
    this.timeInfo,
    this.update({ ...this.criteria, lastUpdated: TimestampFilterKind.all }),
    this.update({
      ...this.criteria,
      lastUpdated: TimestampFilterKind.lastWeek,
    }),
    this.update({
      ...this.criteria,
      lastUpdated: TimestampFilterKind.lastMonth,
    }),
    this.update({
      ...this.criteria,
      lastUpdated: TimestampFilterKind.lastYear,
    }),
    this.update({ ...this.criteria, lastUpdated: TimestampFilterKind.older })
  );
  private readonly allFilters = new Filters<OrphanRouteInfo>(
    this.brokenFilter,
    this.lastUpdatedFilter
  );

  constructor(
    private readonly timeInfo: TimeInfo,
    private readonly criteria: SubsetOrphanRouteFilterCriteria,
    private readonly filterCriteria: BehaviorSubject<SubsetOrphanRouteFilterCriteria>
  ) {}

  filter(routes: OrphanRouteInfo[]): OrphanRouteInfo[] {
    return routes.filter((route) => this.allFilters.passes(route));
  }

  filterOptions(routes: OrphanRouteInfo[]): FilterOptions {
    const totalCount = routes.length;
    const filteredCount = routes.filter((route) =>
      this.allFilters.passes(route)
    ).length;

    const broken = this.brokenFilter.filterOptions(this.allFilters, routes);
    const lastUpdated = this.lastUpdatedFilter.filterOptions(
      this.allFilters,
      routes
    );

    const groups = List([broken, lastUpdated]).filter((g) => g !== null);

    return new FilterOptions(filteredCount, totalCount, groups);
  }

  private update(criteria: SubsetOrphanRouteFilterCriteria) {
    return () => this.filterCriteria.next(criteria);
  }
}
