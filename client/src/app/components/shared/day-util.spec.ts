import { Day } from '@api/custom/day';
import { DayUtil } from './day-util';

describe('day-util', () => {
  it('day parts', () => {
    const day: Day = '2021-08-11';

    expect(DayUtil.year(day)).toEqual('2021');
    expect(DayUtil.month(day)).toEqual('08');
    expect(DayUtil.day(day)).toEqual('11');
  });

  it('toDate and toDay', () => {
    assertToDateToDay('2021-08-11');
    assertToDateToDay('2021-11-01');
    assertToDateToDay('2021-12-31');
    assertToDateToDay('2022-01-01');
  });

  function assertToDateToDay(day: Day) {
    const date = DayUtil.toDate(day);
    const day2 = DayUtil.toDay(date);
    expect(day2).toEqual(day);
  }
});
