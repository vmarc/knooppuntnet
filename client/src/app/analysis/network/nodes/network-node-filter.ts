import { NetworkNodeDetail } from '@api/common/network/network-node-detail';
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
  private readonly definedInNetworkRelationFilter =
    new BooleanFilter<NetworkNodeDetail>(
      'definedInNetworkRelation',
      this.criteria.definedInNetworkRelation,
      (row) => row.definedInRelation,
      this.update({ ...this.criteria, definedInNetworkRelation: null }),
      this.update({ ...this.criteria, definedInNetworkRelation: true }),
      this.update({ ...this.criteria, definedInNetworkRelation: false })
    );

  private readonly definedInRouteRelationFilter =
    new BooleanFilter<NetworkNodeDetail>(
      'definedInRouteRelation',
      this.criteria.definedInRouteRelation,
      (row) => row.definedInRoute,
      this.update({ ...this.criteria, definedInRouteRelation: null }),
      this.update({ ...this.criteria, definedInRouteRelation: true }),
      this.update({ ...this.criteria, definedInRouteRelation: false })
    );

  private readonly referencedInRouteFilter =
    new BooleanFilter<NetworkNodeDetail>(
      'referencedInRoute',
      this.criteria.referencedInRoute,
      (row) => !row.routeReferences.isEmpty(),
      this.update({ ...this.criteria, referencedInRoute: null }),
      this.update({ ...this.criteria, referencedInRoute: true }),
      this.update({ ...this.criteria, referencedInRoute: false })
    );

  private readonly connectionFilter = new BooleanFilter<NetworkNodeDetail>(
    'connection',
    this.criteria.connection,
    (row) => row.connection,
    this.update({ ...this.criteria, connection: null }),
    this.update({ ...this.criteria, connection: true }),
    this.update({ ...this.criteria, connection: false })
  );

  private readonly roleConnectionFilter = new BooleanFilter<NetworkNodeDetail>(
    'roleConnection',
    this.criteria.roleConnection,
    (row) => row.roleConnection,
    this.update({ ...this.criteria, roleConnection: null }),
    this.update({ ...this.criteria, roleConnection: true }),
    this.update({ ...this.criteria, roleConnection: false })
  );

  private readonly integrityCheckFilter = new BooleanFilter<NetworkNodeDetail>(
    'integrityCheck',
    this.criteria.integrityCheck,
    (row) => row.expectedRouteCount !== '-',
    this.update({ ...this.criteria, integrityCheck: null }),
    this.update({ ...this.criteria, integrityCheck: true }),
    this.update({ ...this.criteria, integrityCheck: false })
  );

  private readonly integrityCheckFailedFilter =
    new BooleanFilter<NetworkNodeDetail>(
      'integrityCheckFailed',
      this.criteria.integrityCheckFailed,
      (row) =>
        row.expectedRouteCount === '-'
          ? false
          : +row.expectedRouteCount !== row.routeReferences.size,
      this.update({ ...this.criteria, integrityCheckFailed: null }),
      this.update({ ...this.criteria, integrityCheckFailed: true }),
      this.update({ ...this.criteria, integrityCheckFailed: false })
    );

  private readonly lastUpdatedFilter = new TimestampFilter<NetworkNodeDetail>(
    this.criteria.lastUpdated,
    (row) => row.timestamp,
    this.timeInfo,
    this.update({ ...this.criteria, lastUpdated: TimestampFilterKind.ALL }),
    this.update({
      ...this.criteria,
      lastUpdated: TimestampFilterKind.LAST_WEEK,
    }),
    this.update({
      ...this.criteria,
      lastUpdated: TimestampFilterKind.LAST_MONTH,
    }),
    this.update({
      ...this.criteria,
      lastUpdated: TimestampFilterKind.LAST_YEAR,
    }),
    this.update({ ...this.criteria, lastUpdated: TimestampFilterKind.OLDER })
  );

  private readonly lastSurveyFilter = new SurveyDateFilter<NetworkNodeDetail>(
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

  private readonly allFilters = new Filters<NetworkNodeDetail>(
    this.definedInNetworkRelationFilter,
    this.definedInRouteRelationFilter,
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

  filter(nodes: List<NetworkNodeDetail>): List<NetworkNodeDetail> {
    return nodes.filter((node) => this.allFilters.passes(node));
  }

  filterOptions(nodes: List<NetworkNodeDetail>): FilterOptions {
    const totalCount = nodes.size;
    const filteredCount = nodes.count((node) => this.allFilters.passes(node));

    const definedInNetworkRelation =
      this.definedInNetworkRelationFilter.filterOptions(this.allFilters, nodes);
    const definedInRouteRelation =
      this.definedInRouteRelationFilter.filterOptions(this.allFilters, nodes);
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
      definedInNetworkRelation,
      definedInRouteRelation,
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
