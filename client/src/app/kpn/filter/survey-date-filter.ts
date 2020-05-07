import {List} from "immutable";
import {SurveyDateTimeInfo} from "../api/common/survey-date-time-info";
import {Day} from "../api/custom/day";
import {Filter} from "./filter";
import {FilterOption} from "./filter-option";
import {FilterOptionGroup} from "./filter-option-group";
import {Filters} from "./filters";
import {SurveyDateFilterKind} from "./survey-date-filter-kind";

export class SurveyDateFilter<T> extends Filter<T> {

  constructor(private readonly kind: SurveyDateFilterKind,
              private readonly getter: (arg: T) => Day,
              private readonly timeInfo: SurveyDateTimeInfo,
              private readonly selectAll: () => void,
              private readonly selectUnknown: () => void,
              private readonly selectLastMonth: () => void,
              private readonly selectLastHalfYear: () => void,
              private readonly selectLastYear: () => void,
              private readonly selectLastTwoYears: () => void,
              private readonly selectOlder: () => void) {
    super("lastSurveyDate");
  }

  passes(element: T): boolean {
    if (this.isAll()) {
      return true;
    }
    if (this.isUnknown()) {
      return !this.getter(element);
    }
    if (this.isLastMonth()) {
      return this.getter(element).sameAsOrYoungerThan(this.timeInfo.lastMonthStart);
    }
    if (this.isLastHalfYear()) {
      return this.getter(element).sameAsOrYoungerThan(this.timeInfo.lastHalfYearStart);
    }
    if (this.isLastYear()) {
      return this.getter(element).sameAsOrYoungerThan(this.timeInfo.lastYearStart);
    }
    if (this.isLastTwoYears()) {
      return this.getter(element).sameAsOrYoungerThan(this.timeInfo.lastTwoYearsStart);
    }
    if (this.isOlder()) {
      return this.getter(element).olderThan(this.timeInfo.lastTwoYearsStart);
    }
    return false;
  }

  filterOptions(allFilters: Filters<T>, elements: List<T>): FilterOptionGroup {
    if (elements.isEmpty()) {
      return null;
    }

    const filteredElements = allFilters.filterExcept(elements, this);

    const allCount = filteredElements.size;
    const unknownCount = filteredElements.count(e => !this.getter(e));
    const lastMonthCount = filteredElements.count(e => this.getter(e).sameAsOrYoungerThan(this.timeInfo.lastMonthStart));
    const lastHalfYearCount = filteredElements.count(e => this.getter(e).sameAsOrYoungerThan(this.timeInfo.lastHalfYearStart));
    const lastYearCount = filteredElements.count(e => this.getter(e).sameAsOrYoungerThan(this.timeInfo.lastYearStart));
    const lastTwoYearsCount = filteredElements.count(e => this.getter(e).sameAsOrYoungerThan(this.timeInfo.lastTwoYearsStart));
    const olderCount = filteredElements.count(e => this.getter(e).olderThan(this.timeInfo.lastYearStart));

    const all = new FilterOption("all", allCount, this.isAll(), this.selectAll);
    const unknown = new FilterOption("unknown", unknownCount, this.isUnknown(), this.selectUnknown);
    const lastMonth = new FilterOption("lastMonth", lastMonthCount, this.isLastMonth(), this.selectLastMonth);
    const lastHalfYear = new FilterOption("lastHalfYear", lastHalfYearCount, this.isLastHalfYear(), this.selectLastHalfYear);
    const lastYear = new FilterOption("lastYear", lastYearCount, this.isLastYear(), this.selectLastYear);
    const lastTwoYears = new FilterOption("lastTwoYears", lastTwoYearsCount, this.isLastTwoYears(), this.selectLastTwoYears);
    const older = new FilterOption("older", olderCount, this.isOlder(), this.selectOlder);

    return new FilterOptionGroup(
      this.name,
      all,
      unknown,
      lastMonth,
      lastHalfYear,
      lastYear,
      lastTwoYears,
      older
    );
  }

  private isAll(): boolean {
    return this.kind === SurveyDateFilterKind.ALL;
  }

  private isUnknown(): boolean {
    return this.kind === SurveyDateFilterKind.UNKNOWN;
  }

  private isLastMonth(): boolean {
    return this.kind === SurveyDateFilterKind.LAST_MONTH;
  }

  private isLastHalfYear(): boolean {
    return this.kind === SurveyDateFilterKind.LAST_HALF_YEAR;
  }

  private isLastYear(): boolean {
    return this.kind === SurveyDateFilterKind.LAST_YEAR;
  }

  private isLastTwoYears(): boolean {
    return this.kind === SurveyDateFilterKind.LAST_TWO_YEARS;
  }

  private isOlder(): boolean {
    return this.kind === SurveyDateFilterKind.OLDER;
  }
}
