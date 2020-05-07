import {Day} from "./day";

describe("Day", () => {

  it("sameAs", () => {
    expect(new Day(2020, 8, 11).sameAs(new Day(2020, 8, 11))).toBeTruthy();
    expect(new Day(2020, 8, 11).sameAs(new Day(2020, 8, 10))).toBeFalsy();
    expect(new Day(2020, 8, 11).sameAs(new Day(2020, 8, null))).toBeFalsy();
    expect(new Day(2020, 8, null).sameAs(new Day(2020, 8, 11))).toBeFalsy();
  });

  it("youngerThan", () => {

    expect(new Day(2020, 1, 1).youngerThan(new Day(2021, 1, 1))).toBeFalsy();
    expect(new Day(2021, 1, 1).youngerThan(new Day(2020, 1, 1))).toBeTruthy();

    expect(new Day(2020, 7, 1).youngerThan(new Day(2020, 8, 1))).toBeFalsy();
    expect(new Day(2020, 8, 1).youngerThan(new Day(2020, 7, 1))).toBeTruthy();

    expect(new Day(2020, 8, 10).youngerThan(new Day(2020, 8, 11))).toBeFalsy();
    expect(new Day(2020, 8, 11).youngerThan(new Day(2020, 8, 10))).toBeTruthy();
    expect(new Day(2020, 8, 11).youngerThan(new Day(2020, 8, 11))).toBeFalsy();

    expect(new Day(2020, 8, 11).youngerThan(new Day(2020, 8, null))).toBeTruthy();
    expect(new Day(2020, 8, null).youngerThan(new Day(2020, 8, 11))).toBeFalsy();

    expect(new Day(2020, 7, null).youngerThan(new Day(2020, 8, null))).toBeFalsy();
    expect(new Day(2020, 8, null).youngerThan(new Day(2020, 7, null))).toBeTruthy();

  });

});
