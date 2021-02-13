import {SurveyDateInfo} from '@api/common/survey-date-info';
import {Day} from '@api/custom/day';
import {Filters} from './filters';
import {SurveyDateFilter} from './survey-date-filter';
import {SurveyDateFilterKind} from './survey-date-filter-kind';

describe('SurveyDateFilter', () => {

  it('unknown', () => {

    const filter = buildFilter(SurveyDateFilterKind.UNKNOWN);

    expect(filter.passes(null)).toBeTruthy();
    expect(filter.passes(new Day(2020, 4, 1))).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [null]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(1); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(0); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(0); // older
  });

  it('last month', () => {

    const filter = buildFilter(SurveyDateFilterKind.LAST_MONTH);

    expect(filter.passes(new Day(2020, 5, 1))).toBeTruthy();
    expect(filter.passes(new Day(2020, 4, 1))).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [new Day(2020, 5, 1)]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(1); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(0); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(0); // older
  });

  it('last half year', () => {

    const filter = buildFilter(SurveyDateFilterKind.LAST_HALF_YEAR);

    expect(filter.passes(new Day(2020, 4, 8))).toBeFalsy();
    expect(filter.passes(new Day(2019, 11, 8))).toBeTruthy();
    expect(filter.passes(new Day(2019, 11, 6))).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [new Day(2019, 11, 8)]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(1); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(0); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(0); // older
  });

  it('last year', () => {

    const filter = buildFilter(SurveyDateFilterKind.LAST_YEAR);

    expect(filter.passes(new Day(2020, 4, 8))).toBeFalsy();
    expect(filter.passes(new Day(2019, 5, 8))).toBeTruthy();
    expect(filter.passes(new Day(2020, 5, 6))).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [new Day(2019, 5, 8)]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(1); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(0); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(0); // older
  });

  it('last two years', () => {

    const filter = buildFilter(SurveyDateFilterKind.LAST_TWO_YEARS);

    expect(filter.passes(new Day(2019, 5, 8))).toBeFalsy();
    expect(filter.passes(new Day(2018, 5, 8))).toBeTruthy();
    expect(filter.passes(new Day(2018, 5, 6))).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [new Day(2018, 5, 8)]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(1); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(0); // older
  });

  it('older', () => {

    const filter = buildFilter(SurveyDateFilterKind.OLDER);

    expect(filter.passes(new Day(2018, 5, 8))).toBeFalsy();
    expect(filter.passes(new Day(2018, 5, 6))).toBeTruthy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [new Day(2018, 5, 6)]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(0); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(1); // older
  });

  function doNothing(): void {
  }

  function buildFilter(kind: SurveyDateFilterKind): SurveyDateFilter<Day> {

    const surveyDateInfo = new SurveyDateInfo(
      new Day(2020, 5, 7),
      new Day(2020, 4, 7),
      new Day(2019, 11, 7),
      new Day(2019, 5, 7),
      new Day(2018, 5, 7)
    );

    return new SurveyDateFilter(
      kind,
      (arg: Day) => arg,
      surveyDateInfo,
      doNothing,
      doNothing,
      doNothing,
      doNothing,
      doNothing,
      doNothing,
      doNothing
    );
  }

});
