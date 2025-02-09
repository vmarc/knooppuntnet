import { WritableSignal } from '@angular/core';
import { OrphanRouteInfo } from '@api/common/orphan-route-info';
import { TimeInfo } from '@api/common/time-info';
import { BooleanFilter } from '@app/shared/kpn/filter/boolean-filter';
import { FilterOptions } from '@app/shared/kpn/filter/filter-options';
import { Filters } from '@app/shared/kpn/filter/filters';
import { TimestampFilter } from '@app/shared/kpn/filter/timestamp-filter';
import { TimestampFilterKind } from '@app/shared/kpn/filter/timestamp-filter-kind';
import { SubsetOrphanRouteFilterCriteria } from './subset-orphan-route-filter-criteria';

export class SubsetOrphanRouteFilter {
  private readonly brokenFilter = new BooleanFilter<OrphanRouteInfo>(
    'investigate',
    this.criteria().broken,
    (row) => row.isBroken,
    () => this.criteria.update((c) => ({ ...c, broken: null })),
    () => this.criteria.update((c) => ({ ...c, broken: true })),
    () => this.criteria.update((c) => ({ ...c, broken: false }))
  );
  private readonly lastUpdatedFilter = new TimestampFilter<OrphanRouteInfo>(
    this.criteria().lastUpdated,
    (row) => row.lastUpdated,
    this.timeInfo,
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.all })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.lastWeek })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.lastMonth })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.lastYear })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.older }))
  );
  private readonly allFilters = new Filters<OrphanRouteInfo>(
    this.brokenFilter,
    this.lastUpdatedFilter
  );

  constructor(
    private readonly criteria: WritableSignal<SubsetOrphanRouteFilterCriteria>,
    private readonly timeInfo: TimeInfo
  ) {}

  filter(routes: OrphanRouteInfo[]): OrphanRouteInfo[] {
    return routes.filter((route) => this.allFilters.passes(route));
  }

  filterOptions(routes: OrphanRouteInfo[]): FilterOptions {
    const totalCount = routes.length;
    const filteredCount = routes.filter((route) => this.allFilters.passes(route)).length;

    const broken = this.brokenFilter.filterOptions(this.allFilters, routes);
    const lastUpdated = this.lastUpdatedFilter.filterOptions(this.allFilters, routes);

    const groups = [broken, lastUpdated].filter((g) => g !== null);

    return new FilterOptions(filteredCount, totalCount, groups);
  }
}
