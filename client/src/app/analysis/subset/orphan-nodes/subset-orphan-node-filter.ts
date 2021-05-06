import { OrphanNodeInfo } from '@api/common/orphan-node-info';
import { TimeInfo } from '@api/common/time-info';
import { List } from 'immutable';
import { BehaviorSubject } from 'rxjs';
import { FilterOptions } from '../../../kpn/filter/filter-options';
import { Filters } from '../../../kpn/filter/filters';
import { TimestampFilter } from '../../../kpn/filter/timestamp-filter';
import { TimestampFilterKind } from '../../../kpn/filter/timestamp-filter-kind';
import { SubsetOrphanNodeFilterCriteria } from './subset-orphan-node-filter-criteria';

export class SubsetOrphanNodeFilter {
  private readonly lastUpdatedFilter = new TimestampFilter<OrphanNodeInfo>(
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

  private readonly allFilters = new Filters<OrphanNodeInfo>(
    this.lastUpdatedFilter
  );

  constructor(
    private readonly timeInfo: TimeInfo,
    private readonly criteria: SubsetOrphanNodeFilterCriteria,
    private readonly filterCriteria: BehaviorSubject<SubsetOrphanNodeFilterCriteria>
  ) {}

  filter(nodes: OrphanNodeInfo[]): OrphanNodeInfo[] {
    return nodes.filter((node) => this.allFilters.passes(node));
  }

  filterOptions(nodes: OrphanNodeInfo[]): FilterOptions {
    const totalCount = nodes.length;
    const filteredCount = nodes.filter((node) => this.allFilters.passes(node))
      .length;

    const lastUpdated = this.lastUpdatedFilter.filterOptions(
      this.allFilters,
      nodes
    );

    const groups = List([lastUpdated]).filter((g) => g !== null);

    return new FilterOptions(filteredCount, totalCount, groups);
  }

  private update(criteria: SubsetOrphanNodeFilterCriteria) {
    return () => this.filterCriteria.next(criteria);
  }
}
