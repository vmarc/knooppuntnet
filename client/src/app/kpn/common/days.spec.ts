import { Day } from '@api/custom/day';
import { Days } from './days';

describe('Days', () => {
  it('sameAs', () => {
    expect(
      Days.sameAs(new Day(2020, 8, 11), new Day(2020, 8, 11))
    ).toBeTruthy();
    expect(Days.sameAs(new Day(2020, 8, 11), new Day(2020, 8, 10))).toBeFalsy();
    expect(
      Days.sameAs(new Day(2020, 8, 11), new Day(2020, 8, null))
    ).toBeFalsy();
    expect(
      Days.sameAs(new Day(2020, 8, null), new Day(2020, 8, 11))
    ).toBeFalsy();
  });

  it('youngerThan', () => {
    expect(
      Days.youngerThan(new Day(2020, 1, 1), new Day(2021, 1, 1))
    ).toBeFalsy();
    expect(
      Days.youngerThan(new Day(2021, 1, 1), new Day(2020, 1, 1))
    ).toBeTruthy();

    expect(
      Days.youngerThan(new Day(2020, 7, 1), new Day(2020, 8, 1))
    ).toBeFalsy();
    expect(
      Days.youngerThan(new Day(2020, 8, 1), new Day(2020, 7, 1))
    ).toBeTruthy();

    expect(
      Days.youngerThan(new Day(2020, 8, 10), new Day(2020, 8, 11))
    ).toBeFalsy();
    expect(
      Days.youngerThan(new Day(2020, 8, 11), new Day(2020, 8, 10))
    ).toBeTruthy();
    expect(
      Days.youngerThan(new Day(2020, 8, 11), new Day(2020, 8, 11))
    ).toBeFalsy();

    expect(
      Days.youngerThan(new Day(2020, 8, 11), new Day(2020, 8, null))
    ).toBeTruthy();
    expect(
      Days.youngerThan(new Day(2020, 8, null), new Day(2020, 8, 11))
    ).toBeFalsy();

    expect(
      Days.youngerThan(new Day(2020, 7, null), new Day(2020, 8, null))
    ).toBeFalsy();
    expect(
      Days.youngerThan(new Day(2020, 8, null), new Day(2020, 7, null))
    ).toBeTruthy();
  });
});
