import { Day } from "@api/custom/day";
import { DayUtil } from "./day-util";

describe("day-util", () => {
  it("day parts", () => {
    const day: Day = "2021-08-11";

    expect(DayUtil.year(day)).toEqual("2021");
    expect(DayUtil.month(day)).toEqual("08");
    expect(DayUtil.day(day)).toEqual("11");
  });
});
