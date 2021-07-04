import { NetworkRouteRow } from '@api/common/network/network-route-row';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { List } from 'immutable';
import { BehaviorSubject } from 'rxjs';
import { BooleanFilter } from '@app/kpn/filter/boolean-filter';
import { FilterOptions } from '@app/kpn/filter/filter-options';
import { Filters } from '@app/kpn/filter/filters';
import { SurveyDateFilter } from '@app/kpn/filter/survey-date-filter';
import { SurveyDateFilterKind } from '@app/kpn/filter/survey-date-filter-kind';
import { TimestampFilter } from '@app/kpn/filter/timestamp-filter';
import { TimestampFilterKind } from '@app/kpn/filter/timestamp-filter-kind';
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
    this.update({ ...this.criteria, lastSurvey: SurveyDateFilterKind.ALL }),
    this.update({ ...this.criteria, lastSurvey: SurveyDateFilterKind.UNKNOWN }),
    this.update({
      ...this.criteria,
      lastSurvey: SurveyDateFilterKind.LAST_MONTH,
    }),
    this.update({
      ...this.criteria,
      lastSurvey: SurveyDateFilterKind.LAST_HALF_YEAR,
    }),
    this.update({
      ...this.criteria,
      lastSurvey: SurveyDateFilterKind.LAST_YEAR,
    }),
    this.update({
      ...this.criteria,
      lastSurvey: SurveyDateFilterKind.LAST_TWO_YEARS,
    }),
    this.update({ ...this.criteria, lastSurvey: SurveyDateFilterKind.OLDER })
  );

  private readonly lastUpdatedFilter = new TimestampFilter<NetworkRouteRow>(
    this.criteria.relationLastUpdated,
    (row) => row.lastUpdated,
    this.timeInfo,
    this.update({
      ...this.criteria,
      relationLastUpdated: TimestampFilterKind.ALL,
    }),
    this.update({
      ...this.criteria,
      relationLastUpdated: TimestampFilterKind.LAST_WEEK,
    }),
    this.update({
      ...this.criteria,
      relationLastUpdated: TimestampFilterKind.LAST_MONTH,
    }),
    this.update({
      ...this.criteria,
      relationLastUpdated: TimestampFilterKind.LAST_YEAR,
    }),
    this.update({
      ...this.criteria,
      relationLastUpdated: TimestampFilterKind.OLDER,
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

  filter(routes: List<NetworkRouteRow>): List<NetworkRouteRow> {
    return routes.filter((r) => this.allFilters.passes(r));
  }

  filterOptions(routes: List<NetworkRouteRow>): FilterOptions {
    const totalCount = routes.size;
    const filteredCount = routes.count((route) =>
      this.allFilters.passes(route)
    );

    const proposed = this.proposedFilter.filterOptions(
      this.allFilters,
      routes
    );

    const investigate = this.investigateFilter.filterOptions(
      this.allFilters,
      routes
    );
    const accessible = this.accessibleFilter.filterOptions(
      this.allFilters,
      routes
    );
    const roleConnection = this.roleConnectionFilter.filterOptions(
      this.allFilters,
      routes
    );
    const lastSurvey = this.lastSurveyFilter.filterOptions(
      this.allFilters,
      routes
    );
    const lastUpdated = this.lastUpdatedFilter.filterOptions(
      this.allFilters,
      routes
    );

    const groups = List([
      proposed,
      investigate,
      accessible,
      roleConnection,
      lastSurvey,
      lastUpdated,
    ]).filter((g) => g !== null);

    return new FilterOptions(filteredCount, totalCount, groups);
  }

  private update(criteria: NetworkRouteFilterCriteria) {
    return () => this.filterCriteria.next(criteria);
  }
}
