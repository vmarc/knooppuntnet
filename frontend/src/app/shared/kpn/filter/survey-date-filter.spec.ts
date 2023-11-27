import { SurveyDateInfo } from '@api/common';
import { Day } from '@api/custom';
import { Filters } from './filters';
import { SurveyDateFilter } from './survey-date-filter';
import { SurveyDateFilterKind } from './survey-date-filter-kind';

describe('SurveyDateFilter', () => {
  const doNothing = (): void => {};

  const buildFilter = (kind: SurveyDateFilterKind): SurveyDateFilter<Day> => {
    const surveyDateInfo: SurveyDateInfo = {
      now: '2020-05-07',
      lastMonthStart: '2020-04-07',
      lastHalfYearStart: '2019-11-07',
      lastYearStart: '2019-05-07',
      lastTwoYearsStart: '2018-05-07',
    };

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
  };

  it('unknown', () => {
    const filter = buildFilter(SurveyDateFilterKind.unknown);

    expect(filter.passes(null)).toBeTruthy();
    expect(filter.passes('2020-04-01')).toBeFalsy();

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
    const filter = buildFilter(SurveyDateFilterKind.lastMonth);

    expect(filter.passes('2020-05-01')).toBeTruthy();
    expect(filter.passes('2020-04-01')).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), ['2020-05-01']);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(1); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(0); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(0); // older
  });

  it('last half year', () => {
    const filter = buildFilter(SurveyDateFilterKind.lastHalfYear);

    expect(filter.passes('2020-04-08')).toBeFalsy();
    expect(filter.passes('2019-11-08')).toBeTruthy();
    expect(filter.passes('2019-11-06')).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), ['2019-11-08']);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(1); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(0); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(0); // older
  });

  it('last year', () => {
    const filter = buildFilter(SurveyDateFilterKind.lastYear);

    expect(filter.passes('2020-04-08')).toBeFalsy();
    expect(filter.passes('2019-05-08')).toBeTruthy();
    expect(filter.passes('2020-05-06')).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), ['2019-05-08']);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(1); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(0); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(0); // older
  });

  it('last two years', () => {
    const filter = buildFilter(SurveyDateFilterKind.lastTwoYears);

    expect(filter.passes('2019-05-08')).toBeFalsy();
    expect(filter.passes('2018-05-08')).toBeTruthy();
    expect(filter.passes('2018-05-06')).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), ['2018-05-08']);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(1); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(0); // older
  });

  it('older', () => {
    const filter = buildFilter(SurveyDateFilterKind.older);

    expect(filter.passes('2018-05-08')).toBeFalsy();
    expect(filter.passes('2018-05-06')).toBeTruthy();

    const filterOptionGroup = filter.filterOptions(new Filters(), ['2018-05-06']);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // unknown
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last half year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(5).count).toEqual(0); // last two years
    expect(filterOptionGroup.options.get(6).count).toEqual(1); // older
  });
});
