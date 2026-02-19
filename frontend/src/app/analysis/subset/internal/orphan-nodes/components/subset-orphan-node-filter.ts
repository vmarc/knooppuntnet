import { WritableSignal } from '@angular/core';
import { OrphanNodeInfo } from '@api/common/orphan-node-info';
import { TimeInfo } from '@api/common/time-info';
import { FilterOptions } from '@app/shared/kpn/filter/filter-options';
import { Filters } from '@app/shared/kpn/filter/filters';
import { TimestampFilter } from '@app/shared/kpn/filter/timestamp-filter';
import { TimestampFilterKind } from '@app/shared/kpn/filter/timestamp-filter-kind';
import { SubsetOrphanNodeFilterCriteria } from './subset-orphan-node-filter-criteria';

export class SubsetOrphanNodeFilter {
  private readonly lastUpdatedFilter = new TimestampFilter<OrphanNodeInfo>(
    this.criteria().lastUpdated,
    (row) => row.lastUpdated,
    this.timeInfo,
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.all })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.lastWeek })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.lastMonth })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.lastYear })),
    () => this.criteria.update((c) => ({ ...c, lastUpdated: TimestampFilterKind.older }))
  );

  private readonly allFilters = new Filters<OrphanNodeInfo>(this.lastUpdatedFilter);

  constructor(
    private readonly criteria: WritableSignal<SubsetOrphanNodeFilterCriteria>,
    private readonly timeInfo: TimeInfo
  ) {}

  filter(nodes: ReadonlyArray<OrphanNodeInfo>): ReadonlyArray<OrphanNodeInfo> {
    return nodes.filter((node) => this.allFilters.passes(node));
  }

  filterOptions(nodes: ReadonlyArray<OrphanNodeInfo>): FilterOptions {
    const totalCount = nodes.length;
    const filteredCount = nodes.filter((node) => this.allFilters.passes(node)).length;
    const lastUpdated = this.lastUpdatedFilter.filterOptions(this.allFilters, nodes);
    const groups = [lastUpdated].filter((g) => g !== null);
    return new FilterOptions(filteredCount, totalCount, groups);
  }
}
