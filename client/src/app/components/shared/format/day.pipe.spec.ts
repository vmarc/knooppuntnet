import {Day} from "../../../kpn/api/custom/day";
import { DayPipe } from "./day.pipe";

describe("DayPipe", () => {
  it("transform", () => {
    const pipe = new DayPipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform(null)).toEqual("-");
    expect(pipe.transform(new Day(2020, 8, null))).toEqual("2020-08");
    expect(pipe.transform(new Day(2020, 8, 1))).toEqual("2020-08-01");
  });
});
