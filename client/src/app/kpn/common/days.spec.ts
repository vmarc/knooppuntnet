import { Days } from './days';

describe('Days', () => {
  it('sameAs', () => {
    expect(Days.sameAs('2020-08-11', '2020-08-11')).toBeTruthy();
    expect(Days.sameAs('2020-08-11', '2020-08-10')).toBeFalsy();
    expect(Days.sameAs('2020-08-11', '2020-08')).toBeFalsy();
    expect(Days.sameAs('2020-08', '2020-08-11')).toBeFalsy();
  });

  it('youngerThan', () => {
    expect(Days.youngerThan('2020-01-01', '2021-01-01')).toBeFalsy();
    expect(Days.youngerThan('2021-01-01', '2020-01-01')).toBeTruthy();

    expect(Days.youngerThan('2020-07-01', '2020-08-01')).toBeFalsy();
    expect(Days.youngerThan('2020-08-01', '2020-07-01')).toBeTruthy();

    expect(Days.youngerThan('2020-08-10', '2020-08-11')).toBeFalsy();
    expect(Days.youngerThan('2020-08-11', '2020-08-10')).toBeTruthy();
    expect(Days.youngerThan('2020-08-11', '2020-08-11')).toBeFalsy();

    expect(Days.youngerThan('2020-08-11', '2020-08')).toBeTruthy();
    expect(Days.youngerThan('2020-08', '2020-08-11')).toBeFalsy();

    expect(Days.youngerThan('2020-07', '2020-08')).toBeFalsy();
    expect(Days.youngerThan('2020-08', '2020-07')).toBeTruthy();
  });
});
