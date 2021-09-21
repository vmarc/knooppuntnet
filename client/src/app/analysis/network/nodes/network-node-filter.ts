import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { List } from 'immutable';
import { BehaviorSubject } from 'rxjs';
import { BooleanFilter } from '../../../kpn/filter/boolean-filter';
import { FilterOptions } from '../../../kpn/filter/filter-options';
import { Filters } from '../../../kpn/filter/filters';
import { SurveyDateFilter } from '../../../kpn/filter/survey-date-filter';
import { SurveyDateFilterKind } from '../../../kpn/filter/survey-date-filter-kind';
import { TimestampFilter } from '../../../kpn/filter/timestamp-filter';
import { TimestampFilterKind } from '../../../kpn/filter/timestamp-filter-kind';
import { NetworkNodeFilterCriteria } from './network-node-filter-criteria';

export class NetworkNodeFilter {
  private readonly proposedFilter = new BooleanFilter<NetworkNodeRow>(
    'proposed',
    this.criteria.proposed,
    (row) => row.detail.proposed,
    this.update({ ...this.criteria, proposed: null }),
    this.update({ ...this.criteria, proposed: true }),
    this.update({ ...this.criteria, proposed: false })
  );

  private readonly definedInNetworkRelationFilter =
    new BooleanFilter<NetworkNodeRow>(
      'definedInNetworkRelation',
      this.criteria.definedInNetworkRelation,
      (row) => row.detail.definedInRelation,
      this.update({ ...this.criteria, definedInNetworkRelation: null }),
      this.update({ ...this.criteria, definedInNetworkRelation: true }),
      this.update({ ...this.criteria, definedInNetworkRelation: false })
    );

  private readonly referencedInRouteFilter = new BooleanFilter<NetworkNodeRow>(
    'referencedInRoute',
    this.criteria.referencedInRoute,
    (row) => row.routeReferences.length > 0,
    this.update({ ...this.criteria, referencedInRoute: null }),
    this.update({ ...this.criteria, referencedInRoute: true }),
    this.update({ ...this.criteria, referencedInRoute: false })
  );

  private readonly connectionFilter = new BooleanFilter<NetworkNodeRow>(
    'connection',
    this.criteria.connection,
    (row) => row.detail.connection,
    this.update({ ...this.criteria, connection: null }),
    this.update({ ...this.criteria, connection: true }),
    this.update({ ...this.criteria, connection: false })
  );

  private readonly roleConnectionFilter = new BooleanFilter<NetworkNodeRow>(
    'roleConnection',
    this.criteria.roleConnection,
    (row) => row.detail.roleConnection,
    this.update({ ...this.criteria, roleConnection: null }),
    this.update({ ...this.criteria, roleConnection: true }),
    this.update({ ...this.criteria, roleConnection: false })
  );

  private readonly integrityCheckFilter = new BooleanFilter<NetworkNodeRow>(
    'integrityCheck',
    this.criteria.integrityCheck,
    (row) => !!row.detail.expectedRouteCount,
    this.update({ ...this.criteria, integrityCheck: null }),
    this.update({ ...this.criteria, integrityCheck: true }),
    this.update({ ...this.criteria, integrityCheck: false })
  );

  private readonly integrityCheckFailedFilter =
    new BooleanFilter<NetworkNodeRow>(
      'integrityCheckFailed',
      this.criteria.integrityCheckFailed,
      (row) =>
        !!row.detail.expectedRouteCount
          ? +row.detail.expectedRouteCount !== row.routeReferences.length
          : false,
      this.update({ ...this.criteria, integrityCheckFailed: null }),
      this.update({ ...this.criteria, integrityCheckFailed: true }),
      this.update({ ...this.criteria, integrityCheckFailed: false })
    );

  private readonly lastUpdatedFilter = new TimestampFilter<NetworkNodeRow>(
    this.criteria.lastUpdated,
    (row) => row.detail.timestamp,
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

  private readonly lastSurveyFilter = new SurveyDateFilter<NetworkNodeRow>(
    this.criteria.lastSurvey,
    (row) => row.detail.lastSurvey,
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

  private readonly allFilters = new Filters<NetworkNodeRow>(
    this.proposedFilter,
    this.definedInNetworkRelationFilter,
    this.referencedInRouteFilter,
    this.connectionFilter,
    this.roleConnectionFilter,
    this.integrityCheckFilter,
    this.integrityCheckFailedFilter,
    this.lastUpdatedFilter,
    this.lastSurveyFilter
  );

  constructor(
    private readonly timeInfo: TimeInfo,
    private readonly surveyDateInfo: SurveyDateInfo,
    private readonly criteria: NetworkNodeFilterCriteria,
    private readonly filterCriteria: BehaviorSubject<NetworkNodeFilterCriteria>
  ) {}

  filter(nodes: NetworkNodeRow[]): NetworkNodeRow[] {
    return nodes.filter((node) => this.allFilters.passes(node));
  }

  filterOptions(nodes: NetworkNodeRow[]): FilterOptions {
    const totalCount = nodes.length;
    const filteredCount = nodes.filter((node) =>
      this.allFilters.passes(node)
    ).length;

    const proposed = this.proposedFilter.filterOptions(this.allFilters, nodes);

    const definedInNetworkRelation =
      this.definedInNetworkRelationFilter.filterOptions(this.allFilters, nodes);
    const referencedInRoute = this.referencedInRouteFilter.filterOptions(
      this.allFilters,
      nodes
    );
    const connection = this.connectionFilter.filterOptions(
      this.allFilters,
      nodes
    );
    const roleConnection = this.roleConnectionFilter.filterOptions(
      this.allFilters,
      nodes
    );
    const integrityCheck = this.integrityCheckFilter.filterOptions(
      this.allFilters,
      nodes
    );
    const integrityCheckFailed = this.integrityCheckFailedFilter.filterOptions(
      this.allFilters,
      nodes
    );
    const lastSurvey = this.lastSurveyFilter.filterOptions(
      this.allFilters,
      nodes
    );
    const lastUpdated = this.lastUpdatedFilter.filterOptions(
      this.allFilters,
      nodes
    );

    const groups = List([
      proposed,
      definedInNetworkRelation,
      referencedInRoute,
      connection,
      roleConnection,
      integrityCheck,
      integrityCheckFailed,
      lastSurvey,
      lastUpdated,
    ]).filter((g) => g !== null);

    return new FilterOptions(filteredCount, totalCount, groups);
  }

  private update(criteria: NetworkNodeFilterCriteria) {
    return () => this.filterCriteria.next(criteria);
  }
}
