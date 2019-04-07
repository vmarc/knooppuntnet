import {List} from "immutable";
import {PlanLeg} from "./plan-leg";
import {PlanLegFragment} from "./plan-leg-fragment";

describe("PlanLeg", () => {

  it("distance in meters is 0 when no fragments", () => {

    const fragments: List<PlanLegFragment> = List();
    const leg = new PlanLeg("1", null, null, fragments);

    expect(leg.meters()).toEqual(0);
  });

  it("distance in meters", () => {

    const fragment1 = new PlanLegFragment(null, 10, List());
    const fragment2 = new PlanLegFragment(null, 11, List());
    const fragment3 = new PlanLegFragment(null, 12, List());

    const fragments: List<PlanLegFragment> = List([fragment1, fragment2, fragment3]);

    const leg = new PlanLeg("1", null, null, fragments);

    expect(leg.meters()).toEqual(10 + 11 + 12);
  });

});
