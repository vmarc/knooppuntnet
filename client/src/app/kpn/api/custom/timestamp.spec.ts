import {Timestamp} from './timestamp';

describe('Timestamp', () => {

  it('sameAs', () => {
    expect(new Timestamp(2020, 8, 11, 1, 2, 3).sameAs(new Timestamp(2020, 8, 11, 1, 2, 3))).toBeTruthy();
    expect(new Timestamp(2029, 8, 11, 1, 2, 3).sameAs(new Timestamp(2020, 8, 11, 1, 2, 3))).toBeFalsy();
    expect(new Timestamp(2020, 9, 11, 1, 2, 3).sameAs(new Timestamp(2020, 8, 11, 1, 2, 3))).toBeFalsy();
    expect(new Timestamp(2020, 8, 9, 1, 2, 3).sameAs(new Timestamp(2020, 8, 11, 1, 2, 3))).toBeFalsy();
    expect(new Timestamp(2020, 8, 11, 9, 2, 3).sameAs(new Timestamp(2020, 8, 11, 1, 2, 3))).toBeFalsy();
    expect(new Timestamp(2020, 8, 11, 1, 9, 3).sameAs(new Timestamp(2020, 8, 11, 1, 2, 3))).toBeFalsy();
    expect(new Timestamp(2020, 8, 11, 1, 2, 9).sameAs(new Timestamp(2020, 8, 11, 1, 2, 3))).toBeFalsy();
  });

  it('youngerThan', () => {
    expect(new Timestamp(2020, 8, 11, 12, 13, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeFalsy();
    expect(new Timestamp(2020, 8, 11, 12, 13, 13).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeFalsy();
    expect(new Timestamp(2020, 8, 11, 12, 12, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeFalsy();
    expect(new Timestamp(2020, 8, 11, 11, 13, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeFalsy();
    expect(new Timestamp(2020, 8, 10, 12, 13, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeFalsy();
    expect(new Timestamp(2020, 7, 11, 12, 13, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeFalsy();
    expect(new Timestamp(2019, 8, 11, 12, 13, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeFalsy();

    expect(new Timestamp(2020, 8, 11, 12, 13, 15).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeTruthy();
    expect(new Timestamp(2020, 8, 11, 12, 14, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeTruthy();
    expect(new Timestamp(2020, 8, 11, 13, 13, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeTruthy();
    expect(new Timestamp(2020, 8, 12, 12, 13, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeTruthy();
    expect(new Timestamp(2020, 9, 11, 12, 13, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeTruthy();
    expect(new Timestamp(2021, 8, 11, 12, 13, 14).youngerThan(new Timestamp(2020, 8, 11, 12, 13, 14))).toBeTruthy();

  });

});
