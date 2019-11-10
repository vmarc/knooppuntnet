import {List} from "immutable";
import {BehaviorSubject} from "rxjs";
import {FilterOptions} from "../../../kpn/filter/filter-options";
import {Filters} from "../../../kpn/filter/filters";
import {TimestampFilter} from "../../../kpn/filter/timestamp-filter";
import {TimestampFilterKind} from "../../../kpn/filter/timestamp-filter-kind";
import {NodeInfo} from "../../../kpn/api/common/node-info";
import {TimeInfo} from "../../../kpn/api/common/time-info";
import {SubsetOrphanNodeFilterCriteria} from "./subset-orphan-node-filter-criteria";

export class SubsetOrphanNodeFilter {

  private readonly lastUpdatedFilter = new TimestampFilter<NodeInfo>(
    this.criteria.lastUpdated,
    (row) => row.lastUpdated,
    this.timeInfo,
    this.update({...this.criteria, lastUpdated: TimestampFilterKind.ALL}),
    this.update({...this.criteria, lastUpdated: TimestampFilterKind.LAST_WEEK}),
    this.update({...this.criteria, lastUpdated: TimestampFilterKind.LAST_MONTH}),
    this.update({...this.criteria, lastUpdated: TimestampFilterKind.LAST_YEAR}),
    this.update({...this.criteria, lastUpdated: TimestampFilterKind.OLDER})
  );

  private readonly allFilters = new Filters<NodeInfo>(
    this.lastUpdatedFilter
  );

  constructor(private readonly timeInfo: TimeInfo,
              private readonly criteria: SubsetOrphanNodeFilterCriteria,
              private readonly filterCriteria: BehaviorSubject<SubsetOrphanNodeFilterCriteria>) {
  }

  filter(nodes: List<NodeInfo>): List<NodeInfo> {
    return nodes.filter(node => this.allFilters.passes(node));
  }

  filterOptions(nodes: List<NodeInfo>): FilterOptions {

    const totalCount = nodes.size;
    const filteredCount = nodes.count(node => this.allFilters.passes(node));

    const lastUpdated = this.lastUpdatedFilter.filterOptions(this.allFilters, nodes);

    const groups = List([
      lastUpdated
    ]).filter(g => g !== null);

    return new FilterOptions(
      filteredCount,
      totalCount,
      groups
    );
  }

  private update(criteria: SubsetOrphanNodeFilterCriteria) {
    return () => this.filterCriteria.next(criteria);
  }

}
