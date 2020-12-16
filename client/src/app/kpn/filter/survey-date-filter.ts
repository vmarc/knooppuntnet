import {SurveyDateInfo} from '@api/common/survey-date-info';
import {Day} from '@api/custom/day';
import {List} from 'immutable';
import {Filter} from './filter';
import {FilterOption} from './filter-option';
import {FilterOptionGroup} from './filter-option-group';
import {Filters} from './filters';
import {SurveyDateFilterKind} from './survey-date-filter-kind';

export class SurveyDateFilter<T> extends Filter<T> {

  constructor(private readonly kind: SurveyDateFilterKind,
              private readonly getter: (arg: T) => Day,
              private readonly surveyDateInfo: SurveyDateInfo,
              private readonly selectAll: () => void,
              private readonly selectUnknown: () => void,
              private readonly selectLastMonth: () => void,
              private readonly selectLastHalfYear: () => void,
              private readonly selectLastYear: () => void,
              private readonly selectLastTwoYears: () => void,
              private readonly selectOlder: () => void) {
    super('lastSurvey');
  }

  passes(element: T): boolean {

    if (this.isAll()) {
      return true;
    }

    if (this.isUnknown()) {
      return !this.getter(element);
    }
    if (this.isLastMonth()) {
      return this.sameAsOrYoungerThan(element, this.surveyDateInfo.lastMonthStart);
    }
    if (this.isLastHalfYear()) {
      return this.isBetween(element, this.surveyDateInfo.lastMonthStart, this.surveyDateInfo.lastHalfYearStart);
    }
    if (this.isLastYear()) {
      return this.isBetween(element, this.surveyDateInfo.lastHalfYearStart, this.surveyDateInfo.lastYearStart);
    }
    if (this.isLastTwoYears()) {
      return this.isBetween(element, this.surveyDateInfo.lastYearStart, this.surveyDateInfo.lastTwoYearsStart);
    }
    if (this.isOlder()) {
      return this.olderThan(element, this.surveyDateInfo.lastTwoYearsStart);
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
    const lastMonthCount = filteredElements.count(e => this.sameAsOrYoungerThan(e, this.surveyDateInfo.lastMonthStart));
    const lastHalfYearCount = filteredElements.count(e => this.isBetween(e, this.surveyDateInfo.lastMonthStart, this.surveyDateInfo.lastHalfYearStart));
    const lastYearCount = filteredElements.count(e => this.isBetween(e, this.surveyDateInfo.lastHalfYearStart, this.surveyDateInfo.lastYearStart));
    const lastTwoYearsCount = filteredElements.count(e => this.isBetween(e, this.surveyDateInfo.lastYearStart, this.surveyDateInfo.lastTwoYearsStart));
    const olderCount = filteredElements.count(e => !!this.getter(e) && this.getter(e).olderThan(this.surveyDateInfo.lastTwoYearsStart));

    const all = new FilterOption('all', allCount, this.isAll(), this.selectAll);
    const unknown = new FilterOption('unknown', unknownCount, this.isUnknown(), this.selectUnknown);
    const lastMonth = new FilterOption('lastMonth', lastMonthCount, this.isLastMonth(), this.selectLastMonth);
    const lastHalfYear = new FilterOption('lastHalfYear', lastHalfYearCount, this.isLastHalfYear(), this.selectLastHalfYear);
    const lastYear = new FilterOption('lastYear', lastYearCount, this.isLastYear(), this.selectLastYear);
    const lastTwoYears = new FilterOption('lastTwoYears', lastTwoYearsCount, this.isLastTwoYears(), this.selectLastTwoYears);
    const older = new FilterOption('older', olderCount, this.isOlder(), this.selectOlder);

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

  private sameAsOrYoungerThan(element: T, day: Day): boolean {
    const value = this.getter(element);
    if (!value) {
      return false;
    }
    return value.sameAsOrYoungerThan(day);
  }

  private isBetween(element: T, from: Day, to: Day): boolean {
    const value = this.getter(element);
    if (!value) {
      return false;
    }
    return value.olderThan(from) && value.sameAsOrYoungerThan(to);
  }

  private olderThan(element: T, day: Day): boolean {
    const value = this.getter(element);
    if (!value) {
      return false;
    }
    return value.olderThan(day);
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
