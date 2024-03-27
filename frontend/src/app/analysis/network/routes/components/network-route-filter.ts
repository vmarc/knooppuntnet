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
import { BehaviorSubject } from 'rxjs';
import { NetworkRouteFilterCriteria } from './network-route-filter-criteria';

export class NetworkRouteFilter {
  private readonly proposedFilter = new BooleanFilter<NetworkRouteRow>(
    'proposed',
    this.criteria.proposed,
    (row) => row.proposed,
    this.update({ ...this.criteria, proposed: null }),
    this.update({ ...this.criteria, proposed: true }),
    this.update({ ...this.criteria, proposed: false })
  );

  private readonly investigateFilter = new BooleanFilter<NetworkRouteRow>(
    'investigate',
    this.criteria.investigate,
    (row) => row.investigate,
    this.update({ ...this.criteria, investigate: null }),
    this.update({ ...this.criteria, investigate: true }),
    this.update({ ...this.criteria, investigate: false })
  );

  private readonly accessibleFilter = new BooleanFilter<NetworkRouteRow>(
    'accessible',
    this.criteria.accessible,
    (row) => row.accessible,
    this.update({ ...this.criteria, accessible: null }),
    this.update({ ...this.criteria, accessible: true }),
    this.update({ ...this.criteria, accessible: false })
  );

  private readonly roleConnectionFilter = new BooleanFilter<NetworkRouteRow>(
    'connection',
    this.criteria.roleConnection,
    (row) => row.roleConnection,
    this.update({ ...this.criteria, roleConnection: null }),
    this.update({ ...this.criteria, roleConnection: true }),
    this.update({ ...this.criteria, roleConnection: false })
  );

  private readonly lastSurveyFilter = new SurveyDateFilter<NetworkRouteRow>(
    this.criteria.lastSurvey,
    (row) => row.lastSurvey,
    this.surveyDateInfo,
    this.update({ ...this.criteria, lastSurvey: SurveyDateFilterKind.all }),
    this.update({ ...this.criteria, lastSurvey: SurveyDateFilterKind.unknown }),
    this.update({
      ...this.criteria,
      lastSurvey: SurveyDateFilterKind.lastMonth,
    }),
    this.update({
      ...this.criteria,
      lastSurvey: SurveyDateFilterKind.lastHalfYear,
    }),
    this.update({
      ...this.criteria,
      lastSurvey: SurveyDateFilterKind.lastYear,
    }),
    this.update({
      ...this.criteria,
      lastSurvey: SurveyDateFilterKind.lastTwoYears,
    }),
    this.update({ ...this.criteria, lastSurvey: SurveyDateFilterKind.older })
  );

  private readonly lastUpdatedFilter = new TimestampFilter<NetworkRouteRow>(
    this.criteria.relationLastUpdated,
    (row) => row.lastUpdated,
    this.timeInfo,
    this.update({
      ...this.criteria,
      relationLastUpdated: TimestampFilterKind.all,
    }),
    this.update({
      ...this.criteria,
      relationLastUpdated: TimestampFilterKind.lastWeek,
    }),
    this.update({
      ...this.criteria,
      relationLastUpdated: TimestampFilterKind.lastMonth,
    }),
    this.update({
      ...this.criteria,
      relationLastUpdated: TimestampFilterKind.lastYear,
    }),
    this.update({
      ...this.criteria,
      relationLastUpdated: TimestampFilterKind.older,
    })
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
    private timeInfo: TimeInfo,
    private surveyDateInfo: SurveyDateInfo,
    private criteria: NetworkRouteFilterCriteria,
    private filterCriteria: BehaviorSubject<NetworkRouteFilterCriteria>
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

  private update(criteria: NetworkRouteFilterCriteria) {
    return () => this.filterCriteria.next(criteria);
  }
}
