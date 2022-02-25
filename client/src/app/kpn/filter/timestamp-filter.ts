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
      return this.testLastWeek(element);
    }
    if (this.isLastMonth()) {
      return this.testLastMonth(element);
    }
    if (this.isLastYear()) {
      return this.testLastYear(element);
    }
    if (this.isOlder()) {
      return this.testOlder(element);
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
      this.testLastWeek(e)
    ).length;
    const lastMonthCount = filteredElements.filter((e) =>
      this.testLastMonth(e)
    ).length;
    const lastYearCount = filteredElements.filter((e) =>
      this.testLastYear(e)
    ).length;
    const olderCount = filteredElements.filter((e) => this.testOlder(e)).length;

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
    return this.getter(element) >= timestamp;
  }

  private isBetween(element: T, from: Timestamp, to: Timestamp): boolean {
    const value = this.getter(element);
    return value > from && value <= to;
  }

  private olderThan(element: T, timestamp: Timestamp): boolean {
    return this.getter(element) < timestamp;
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

  private testLastWeek(element: T): boolean {
    return this.sameAsOrYoungerThan(element, this.timeInfo.lastWeekStart);
  }

  private testLastMonth(element: T): boolean {
    return this.isBetween(
      element,
      this.timeInfo.lastMonthStart,
      this.timeInfo.lastWeekStart
    );
  }

  private testLastYear(element: T): boolean {
    return this.isBetween(
      element,
      this.timeInfo.lastYearStart,
      this.timeInfo.lastMonthStart
    );
  }

  private testOlder(element: T): boolean {
    return this.olderThan(element, this.timeInfo.lastYearStart);
  }
}
