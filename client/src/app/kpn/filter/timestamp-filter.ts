import { TimeInfo } from '@api/common/time-info';
import { Timestamp } from '@api/custom/timestamp';
import { Filter } from './filter';
import { FilterOption } from './filter-option';
import { FilterOptionGroup } from './filter-option-group';
import { Filters } from './filters';
import { TimestampFilterKind } from './timestamp-filter-kind';

export class TimestampFilter<T> extends Filter<T> {
  constructor(
    private readonly kind: TimestampFilterKind,
    private readonly getter: (arg: T) => Timestamp,
    private readonly timeInfo: TimeInfo,
    private readonly selectAll: () => void,
    private readonly selectLastWeek: () => void,
    private readonly selectLastMonth: () => void,
    private readonly selectLastYear: () => void,
    private readonly selectOlder: () => void
  ) {
    super('lastUpdated');
  }

  passes(element: T): boolean {
    if (this.isAll()) {
      return true;
    }
    if (this.isLastWeek()) {
      return this.sameAsOrYoungerThan(element, this.timeInfo.lastWeekStart);
    }
    if (this.isLastMonth()) {
      return this.isBetween(
        element,
        this.timeInfo.lastWeekStart,
        this.timeInfo.lastMonthStart
      );
    }
    if (this.isLastYear()) {
      return this.isBetween(
        element,
        this.timeInfo.lastMonthStart,
        this.timeInfo.lastYearStart
      );
    }
    if (this.isOlder()) {
      return this.olderThan(element, this.timeInfo.lastYearStart);
    }
    return false;
  }

  filterOptions(allFilters: Filters<T>, elements: T[]): FilterOptionGroup {
    if (elements.length === 0) {
      return null;
    }

    const filteredElements = allFilters.filterExcept(elements, this);

    const allCount = filteredElements.length;
    const lastWeekCount = filteredElements.filter((e) =>
      this.sameAsOrYoungerThan(e, this.timeInfo.lastWeekStart)
    ).length;
    const lastMonthCount = filteredElements.filter((e) =>
      this.isBetween(
        e,
        this.timeInfo.lastWeekStart,
        this.timeInfo.lastMonthStart
      )
    ).length;
    const lastYearCount = filteredElements.filter((e) =>
      this.isBetween(
        e,
        this.timeInfo.lastMonthStart,
        this.timeInfo.lastYearStart
      )
    ).length;
    const olderCount = filteredElements.filter((e) =>
      this.getter(e).olderThan(this.timeInfo.lastYearStart)
    ).length;

    const all = new FilterOption('all', allCount, this.isAll(), this.selectAll);
    const lastWeek = new FilterOption(
      'lastWeek',
      lastWeekCount,
      this.isLastWeek(),
      this.selectLastWeek
    );
    const lastMonth = new FilterOption(
      'lastMonth',
      lastMonthCount,
      this.isLastMonth(),
      this.selectLastMonth
    );
    const lastYear = new FilterOption(
      'lastYear',
      lastYearCount,
      this.isLastYear(),
      this.selectLastYear
    );
    const older = new FilterOption(
      'older',
      olderCount,
      this.isOlder(),
      this.selectOlder
    );

    return new FilterOptionGroup(
      this.name,
      all,
      lastWeek,
      lastMonth,
      lastYear,
      older
    );
  }

  private sameAsOrYoungerThan(element: T, timestamp: Timestamp): boolean {
    return this.getter(element).sameAsOrYoungerThan(timestamp);
  }

  private isBetween(element: T, from: Timestamp, to: Timestamp): boolean {
    const value = this.getter(element);
    return value.olderThan(from) && value.sameAsOrYoungerThan(to);
  }

  private olderThan(element: T, timestamp: Timestamp): boolean {
    return this.getter(element).olderThan(timestamp);
  }

  private isAll(): boolean {
    return this.kind === TimestampFilterKind.all;
  }

  private isLastWeek(): boolean {
    return this.kind === TimestampFilterKind.lastWeek;
  }

  private isLastMonth(): boolean {
    return this.kind === TimestampFilterKind.lastMonth;
  }

  private isLastYear(): boolean {
    return this.kind === TimestampFilterKind.lastYear;
  }

  private isOlder(): boolean {
    return this.kind === TimestampFilterKind.older;
  }
}
