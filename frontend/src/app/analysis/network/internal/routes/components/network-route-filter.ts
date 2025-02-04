import { WritableSignal } from '@angular/core';
import { SurveyDateInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { NetworkRouteRow } from '@api/common/network';
import { BooleanFilter } from '@app/kpn/filter';
import { FilterOptions } from '@app/kpn/filter';
import { Filters } from '@app/kpn/filter';
import { SurveyDateFilter } from '@app/kpn/filter';
import { SurveyDateFilterKind } from '@app/kpn/filter';
import { TimestampFilter } from '@app/kpn/filter';
import { TimestampFilterKind } from '@app/kpn/filter';
import { NetworkRouteFilterCriteria } from './network-route-filter-criteria';

export class NetworkRouteFilter {
  private readonly proposedFilter = new BooleanFilter<NetworkRouteRow>(
    'proposed',
    this.criteria().proposed,
    (row) => row.proposed,
    () => this.criteria.update((c) => ({ ...c, proposed: null })),
    () => this.criteria.update((c) => ({ ...c, proposed: true })),
    () => this.criteria.update((c) => ({ ...c, proposed: false }))
  );

  private readonly investigateFilter = new BooleanFilter<NetworkRouteRow>(
    'investigate',
    this.criteria().investigate,
    (row) => row.investigate,
    () => this.criteria.update((c) => ({ ...c, investigate: null })),
    () => this.criteria.update((c) => ({ ...c, investigate: true })),
    () => this.criteria.update((c) => ({ ...c, investigate: false }))
  );

  private readonly accessibleFilter = new BooleanFilter<NetworkRouteRow>(
    'accessible',
    this.criteria().accessible,
    (row) => row.accessible,
    () => this.criteria.update((c) => ({ ...c, accessible: null })),
    () => this.criteria.update((c) => ({ ...c, accessible: true })),
    () => this.criteria.update((c) => ({ ...c, accessible: false }))
  );

  private readonly roleConnectionFilter = new BooleanFilter<NetworkRouteRow>(
    'connection',
    this.criteria().roleConnection,
    (row) => row.roleConnection,
    () => this.criteria.update((c) => ({ ...c, roleConnection: null })),
    () => this.criteria.update((c) => ({ ...c, roleConnection: true })),
    () => this.criteria.update((c) => ({ ...c, roleConnection: false }))
  );

  private readonly lastSurveyFilter = new SurveyDateFilter<NetworkRouteRow>(
    this.criteria().lastSurvey,
    (row) => row.lastSurvey,
    this.surveyDateInfo,
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.all })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.unknown })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.lastMonth })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.lastHalfYear })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.lastYear })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.lastTwoYears })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.older }))
  );

  private readonly lastUpdatedFilter = new TimestampFilter<NetworkRouteRow>(
    this.criteria().relationLastUpdated,
    (row) => row.lastUpdated,
    this.timeInfo,
    () => this.criteria.update((c) => ({ ...c, relationLastUpdated: TimestampFilterKind.all })),
    () =>
      this.criteria.update((c) => ({ ...c, relationLastUpdated: TimestampFilterKind.lastWeek })),
    () =>
      this.criteria.update((c) => ({ ...c, relationLastUpdated: TimestampFilterKind.lastMonth })),
    () =>
      this.criteria.update((c) => ({ ...c, relationLastUpdated: TimestampFilterKind.lastYear })),
    () => this.criteria.update((c) => ({ ...c, relationLastUpdated: TimestampFilterKind.older }))
  );

  private readonly allFilters = new Filters<NetworkRouteRow>(
    this.proposedFilter,
    this.investigateFilter,
    this.accessibleFilter,
    this.roleConnectionFilter,
    this.lastSurveyFilter,
    this.lastUpdatedFilter
  );

  constructor(
    private criteria: WritableSignal<NetworkRouteFilterCriteria>,
    private timeInfo: TimeInfo,
    private surveyDateInfo: SurveyDateInfo
  ) {}

  filter(routes: NetworkRouteRow[]): NetworkRouteRow[] {
    return routes.filter((r) => this.allFilters.passes(r));
  }

  filterOptions(routes: NetworkRouteRow[]): FilterOptions {
    const totalCount = routes.length;
    const filteredCount = routes.filter((route) => this.allFilters.passes(route)).length;

    const proposed = this.proposedFilter.filterOptions(this.allFilters, routes);

    const investigate = this.investigateFilter.filterOptions(this.allFilters, routes);
    const accessible = this.accessibleFilter.filterOptions(this.allFilters, routes);
    const roleConnection = this.roleConnectionFilter.filterOptions(this.allFilters, routes);
    const lastSurvey = this.lastSurveyFilter.filterOptions(this.allFilters, routes);
    const lastUpdated = this.lastUpdatedFilter.filterOptions(this.allFilters, routes);

    const groups = [
      proposed,
      investigate,
      accessible,
      roleConnection,
      lastSurvey,
      lastUpdated,
    ].filter((g) => g !== null);

    return new FilterOptions(filteredCount, totalCount, groups);
  }
}
