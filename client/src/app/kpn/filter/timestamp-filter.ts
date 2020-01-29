import {Filter} from "./filter";
import {TimestampFilterKind} from "./timestamp-filter-kind";
import {Timestamp} from "../api/custom/timestamp";
import {TimeInfo} from "../api/common/time-info";
import {Filters} from "./filters";
import {FilterOptionGroup} from "./filter-option-group";
import {List} from "immutable";
import {FilterOption} from "./filter-option";

export class TimestampFilter<T> extends Filter<T> {

  constructor(private readonly kind: TimestampFilterKind,
              private readonly getter: (T) => Timestamp,
              private readonly timeInfo: TimeInfo,
              private readonly selectAll: () => void,
              private readonly selectLastWeek: () => void,
              private readonly selectLastMonth: () => void,
              private readonly selectLastYear: () => void,
              private readonly selectOlder: () => void) {
    super("lastUpdated");
  }

  passes(element: T): boolean {
    if (this.isAll()) {
      return true;
    }
    if (this.isLastWeek()) {
      return this.getter(element).sameAsOrYoungerThan(this.timeInfo.lastWeekStart);
    }
    if (this.isLastMonth()) {
      return this.getter(element).sameAsOrYoungerThan(this.timeInfo.lastMonthStart);
    }
    if (this.isLastYear()) {
      return this.getter(element).sameAsOrYoungerThan(this.timeInfo.lastYearStart);
    }
    if (this.isOlder()) {
      return this.getter(element).olderThan(this.timeInfo.lastYearStart);
    }
    return false;
  }

  filterOptions(allFilters: Filters<T>, elements: List<T>): FilterOptionGroup {
    if (elements.isEmpty()) {
      return null;
    }

    const filteredElements = allFilters.filterExcept(elements, this);

    const allCount = filteredElements.size;
    const lastWeekCount = filteredElements.count(e => this.getter(e).sameAsOrYoungerThan(this.timeInfo.lastWeekStart));
    const lastMonthCount = filteredElements.count(e => this.getter(e).sameAsOrYoungerThan(this.timeInfo.lastMonthStart));
    const lastYearCount = filteredElements.count(e => this.getter(e).sameAsOrYoungerThan(this.timeInfo.lastYearStart));
    const olderCount = filteredElements.count(e => this.getter(e).olderThan(this.timeInfo.lastYearStart));

    const all = new FilterOption("all", allCount, this.isAll(), this.selectAll);
    const lastWeek = new FilterOption("lastWeek", lastWeekCount, this.isLastWeek(), this.selectLastWeek);
    const lastMonth = new FilterOption("lastMonth", lastMonthCount, this.isLastMonth(), this.selectLastMonth);
    const lastYear = new FilterOption("lastYear", lastYearCount, this.isLastYear(), this.selectLastYear);
    const older = new FilterOption("older", olderCount, this.isOlder(), this.selectOlder);

    return new FilterOptionGroup(this.name, all, lastWeek, lastMonth, lastYear, older);
  }

  private isAll(): boolean {
    return this.kind === TimestampFilterKind.ALL;
  }

  private isLastWeek(): boolean {
    return this.kind === TimestampFilterKind.LAST_WEEK;
  }

  private isLastMonth(): boolean {
    return this.kind === TimestampFilterKind.LAST_MONTH;
  }

  private isLastYear(): boolean {
    return this.kind === TimestampFilterKind.LAST_YEAR;
  }

  private isOlder(): boolean {
    return this.kind === TimestampFilterKind.OLDER;
  }

}
