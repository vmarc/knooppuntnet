import { WritableSignal } from '@angular/core';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { BooleanFilter } from '@app/shared/kpn/filter/boolean-filter';
import { FilterOptions } from '@app/shared/kpn/filter/filter-options';
import { Filters } from '@app/shared/kpn/filter/filters';
import { SurveyDateFilter } from '@app/shared/kpn/filter/survey-date-filter';
import { SurveyDateFilterKind } from '@app/shared/kpn/filter/survey-date-filter-kind';
import { TimestampFilter } from '@app/shared/kpn/filter/timestamp-filter';
import { TimestampFilterKind } from '@app/shared/kpn/filter/timestamp-filter-kind';
import { NetworkNodeFilterCriteria } from './network-node-filter-criteria';

export class NetworkNodeFilter {
  private readonly proposedFilter = new BooleanFilter<NetworkNodeRow>(
    'proposed',
    this.criteria().proposed,
    (row) => row.detail.proposed,
    () => this.criteria.update((c) => ({ ...c, proposed: null })),
    () => this.criteria.update((c) => ({ ...c, proposed: true })),
    () => this.criteria.update((c) => ({ ...c, proposed: false }))
  );

  private readonly definedInNetworkRelationFilter = new BooleanFilter<NetworkNodeRow>(
    'definedInNetworkRelation',
    this.criteria().definedInNetworkRelation,
    (row) => row.detail.definedInRelation,
    () => this.criteria.update((c) => ({ ...c, definedInNetworkRelation: null })),
    () => this.criteria.update((c) => ({ ...c, definedInNetworkRelation: true })),
    () => this.criteria.update((c) => ({ ...c, definedInNetworkRelation: false }))
  );

  private readonly referencedInRouteFilter = new BooleanFilter<NetworkNodeRow>(
    'referencedInRoute',
    this.criteria().referencedInRoute,
    (row) => row.routeReferences.length > 0,
    () => this.criteria.update((c) => ({ ...c, referencedInRoute: null })),
    () => this.criteria.update((c) => ({ ...c, referencedInRoute: true })),
    () => this.criteria.update((c) => ({ ...c, referencedInRoute: false }))
  );

  private readonly connectionFilter = new BooleanFilter<NetworkNodeRow>(
    'connection',
    this.criteria().connection,
    (row) => row.detail.connection,
    () => this.criteria.update((c) => ({ ...c, connection: null })),
    () => this.criteria.update((c) => ({ ...c, connection: true })),
    () => this.criteria.update((c) => ({ ...c, connection: false }))
  );

  private readonly roleConnectionFilter = new BooleanFilter<NetworkNodeRow>(
    'roleConnection',
    this.criteria().roleConnection,
    (row) => row.detail.roleConnection,
    () => this.criteria.update((c) => ({ ...c, roleConnection: null })),
    () => this.criteria.update((c) => ({ ...c, roleConnection: true })),
    () => this.criteria.update((c) => ({ ...c, roleConnection: false }))
  );

  private readonly integrityCheckFilter = new BooleanFilter<NetworkNodeRow>(
    'integrityCheck',
    this.criteria().integrityCheck,
    (row) => !!row.detail.expectedRouteCount,
    () => this.criteria.update((c) => ({ ...c, integrityCheck: null })),
    () => this.criteria.update((c) => ({ ...c, integrityCheck: true })),
    () => this.criteria.update((c) => ({ ...c, integrityCheck: false }))
  );

  private readonly integrityCheckFailedFilter = new BooleanFilter<NetworkNodeRow>(
    'integrityCheckFailed',
    this.criteria().integrityCheckFailed,
    (row) =>
      row.detail.expectedRouteCount
        ? +row.detail.expectedRouteCount !== row.routeReferences.length
        : false,
    () => this.criteria.update((c) => ({ ...c, integrityCheckFailed: null })),
    () => this.criteria.update((c) => ({ ...c, integrityCheckFailed: true })),
    () => this.criteria.update((c) => ({ ...c, integrityCheckFailed: false }))
  );

  private readonly lastUpdatedFilter = new TimestampFilter<NetworkNodeRow>(
    this.criteria().lastUpdated,
    (row) => row.detail.timestamp,
    this.timeInfo,
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.all })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.lastWeek })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.lastMonth })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.lastYear })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.older }))
  );

  private readonly lastSurveyFilter = new SurveyDateFilter<NetworkNodeRow>(
    this.criteria().lastSurvey,
    (row) => row.detail.lastSurvey,
    this.surveyDateInfo,
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.all })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.unknown })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.lastMonth })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.lastHalfYear })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.lastYear })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.lastTwoYears })),
    () => this.criteria.update((c) => ({ ...c, lastSurvey: SurveyDateFilterKind.older }))
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
    private readonly criteria: WritableSignal<NetworkNodeFilterCriteria>,
    private readonly surveyDateInfo: SurveyDateInfo,
    private readonly timeInfo: TimeInfo
  ) {}

  filter(nodes: ReadonlyArray<NetworkNodeRow>): ReadonlyArray<NetworkNodeRow> {
    return nodes.filter((node) => this.allFilters.passes(node));
  }

  filterOptions(nodes: ReadonlyArray<NetworkNodeRow>): FilterOptions {
    const totalCount = nodes.length;
    const filteredCount = nodes.filter((node) => this.allFilters.passes(node)).length;

    const proposed = this.proposedFilter.filterOptions(this.allFilters, nodes);
    const definedInNetworkRelation = this.definedInNetworkRelationFilter.filterOptions(
      this.allFilters,
      nodes
    );
    const referencedInRoute = this.referencedInRouteFilter.filterOptions(this.allFilters, nodes);
    const connection = this.connectionFilter.filterOptions(this.allFilters, nodes);
    const roleConnection = this.roleConnectionFilter.filterOptions(this.allFilters, nodes);
    const integrityCheck = this.integrityCheckFilter.filterOptions(this.allFilters, nodes);
    const integrityCheckFailed = this.integrityCheckFailedFilter.filterOptions(
      this.allFilters,
      nodes
    );
    const lastSurvey = this.lastSurveyFilter.filterOptions(this.allFilters, nodes);
    const lastUpdated = this.lastUpdatedFilter.filterOptions(this.allFilters, nodes);

    const groups = [
      proposed,
      definedInNetworkRelation,
      referencedInRoute,
      connection,
      roleConnection,
      integrityCheck,
      integrityCheckFailed,
      lastSurvey,
      lastUpdated,
    ].filter((g) => g !== null);

    return new FilterOptions(filteredCount, totalCount, groups);
  }
}
