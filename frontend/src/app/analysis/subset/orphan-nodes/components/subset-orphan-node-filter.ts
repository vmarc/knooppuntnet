import { WritableSignal } from '@angular/core';
import { OrphanNodeInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { FilterOptions } from '@app/kpn/filter';
import { Filters } from '@app/kpn/filter';
import { TimestampFilter } from '@app/kpn/filter';
import { TimestampFilterKind } from '@app/kpn/filter';
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

  filter(nodes: OrphanNodeInfo[]): OrphanNodeInfo[] {
    return nodes.filter((node) => this.allFilters.passes(node));
  }

  filterOptions(nodes: OrphanNodeInfo[]): FilterOptions {
    const totalCount = nodes.length;
    const filteredCount = nodes.filter((node) => this.allFilters.passes(node)).length;
    const lastUpdated = this.lastUpdatedFilter.filterOptions(this.allFilters, nodes);
    const groups = [lastUpdated].filter((g) => g !== null);
    return new FilterOptions(filteredCount, totalCount, groups);
  }
}
