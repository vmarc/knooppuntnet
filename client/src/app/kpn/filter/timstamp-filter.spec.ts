import { TimeInfo } from '@api/common/time-info';
import { Timestamp } from '@api/custom/timestamp';
import { Filters } from './filters';
import { TimestampFilter } from './timestamp-filter';
import { TimestampFilterKind } from './timestamp-filter-kind';

describe('TimestampFilter', () => {
  const doNothing = (): void => {};

  const buildFilter = (
    kind: TimestampFilterKind
  ): TimestampFilter<Timestamp> => {
    const timeInfo: TimeInfo = {
      now: new Timestamp(2020, 5, 7, 0, 0, 0),
      lastWeekStart: new Timestamp(2020, 5, 1, 0, 0, 0),
      lastMonthStart: new Timestamp(2020, 4, 7, 0, 0, 0),
      lastYearStart: new Timestamp(2019, 5, 7, 0, 0, 0),
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

    expect(filter.passes(new Timestamp(2020, 5, 6, 0, 0, 0))).toBeTruthy();
    expect(filter.passes(new Timestamp(2020, 4, 1, 0, 0, 0))).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [
      new Timestamp(2020, 5, 6, 0, 0, 0),
    ]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(1); // last week
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // older
  });

  it('last month', () => {
    const filter = buildFilter(TimestampFilterKind.lastMonth);

    expect(filter.passes(new Timestamp(2020, 5, 2, 0, 0, 0))).toBeFalsy();
    expect(filter.passes(new Timestamp(2020, 4, 8, 0, 0, 0))).toBeTruthy();
    expect(filter.passes(new Timestamp(2020, 4, 6, 0, 0, 0))).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [
      new Timestamp(2020, 4, 8, 0, 0, 0),
    ]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // last week
    expect(filterOptionGroup.options.get(2).count).toEqual(1); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // older
  });

  it('last year', () => {
    const filter = buildFilter(TimestampFilterKind.lastYear);

    expect(filter.passes(new Timestamp(2019, 5, 6, 0, 0, 0))).toBeFalsy();
    expect(filter.passes(new Timestamp(2019, 5, 8, 0, 0, 0))).toBeTruthy();
    expect(filter.passes(new Timestamp(2019, 5, 6, 0, 0, 0))).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [
      new Timestamp(2019, 5, 8, 0, 0, 0),
    ]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // last week
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(1); // last year
    expect(filterOptionGroup.options.get(4).count).toEqual(0); // older
  });

  it('older', () => {
    const filter = buildFilter(TimestampFilterKind.older);

    expect(filter.passes(new Timestamp(2019, 5, 6, 0, 0, 0))).toBeTruthy();
    expect(filter.passes(new Timestamp(2019, 5, 8, 0, 0, 0))).toBeFalsy();

    const filterOptionGroup = filter.filterOptions(new Filters(), [
      new Timestamp(2019, 5, 6, 0, 0, 0),
    ]);
    expect(filterOptionGroup.options.get(0).count).toEqual(1); // all
    expect(filterOptionGroup.options.get(1).count).toEqual(0); // last week
    expect(filterOptionGroup.options.get(2).count).toEqual(0); // last month
    expect(filterOptionGroup.options.get(3).count).toEqual(0); // last year
    expect(filterOptionGroup.options.get(4).count).toEqual(1); // older
  });
});
