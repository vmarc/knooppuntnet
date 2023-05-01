import { TimeInfo } from '@api/common';
import { Timestamp } from '@api/custom';
import { Filters } from './filters';
import { TimestampFilter } from './timestamp-filter';
import { TimestampFilterKind } from './timestamp-filter-kind';

describe('TimestampFilter', () => {
  const doNothing = (): void => {};

  const buildFilter = (
    kind: TimestampFilterKind
  ): TimestampFilter<Timestamp> => {
    const timeInfo: TimeInfo = {
      now: '2020-05-07 00:00:00',
      lastWeekStart: '2020-05-01 00:00:00',
      lastMonthStart: '2020-04-07 00:00:00',
      lastYearStart: '2019-05-07 00:00:00',
    };

    return new TimestampFilter(
      kind,
      (arg: Timestamp) => arg,
      timeInfo,
      doNothing,
      doNothing,
      doNothing,
      doNothing,
      doNothing
    );
  };

  it('last week', () => {
    const filter = buildFilter(TimestampFilterKind.lastWeek);

    expect(filter.passes('2020-05-06 00:00:00')).toBeTruthy();
    expect(filter.passes('2020-04-01 00:00:00')).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [
      '2020-05-06 00:00:00',
    ]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(1); // last week
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // older
  });

  it('last month', () => {
    const filter = buildFilter(TimestampFilterKind.lastMonth);

    expect(filter.passes('2020-05-02 00:00:00')).toBeFalsy();
    expect(filter.passes('2020-04-08 00:00:00')).toBeTruthy();
    expect(filter.passes('2020-04-06 00:00:00')).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [
      '2020-04-08 00:00:00',
    ]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // last week
    expect(filterOptionGroup.options.get(2).count).toEqual(1); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // older
  });

  it('last year', () => {
    const filter = buildFilter(TimestampFilterKind.lastYear);

    expect(filter.passes('2019-05-06 00:00:00')).toBeFalsy();
    expect(filter.passes('2019-05-08 00:00:00')).toBeTruthy();
    expect(filter.passes('2019-05-06 00:00:00')).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [
      '2019-05-08 00:00:00',
    ]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // last week
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(1); // last year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // older
  });

  it('older', () => {
    const filter = buildFilter(TimestampFilterKind.older);

    expect(filter.passes('2019-05-06 00:00:00')).toBeTruthy();
    expect(filter.passes('2019-05-08 00:00:00')).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [
      '2019-05-06 00:00:00',
    ]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // last week
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(4).count).toEqual(1); // older
  });
});
