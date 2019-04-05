import {List} from "immutable";
import {Plan} from "./plan";
import {PlanLeg} from "./plan-leg";
import {PlanLegFragment} from "./plan-leg-fragment";

describe("Plan", () => {

  it("total distance empty plan", () => {

    const plan = Plan.empty();

    expect(plan.cumulativeKmLeg(0)).toEqual("0 km");
  });

  it("total distance", () => {

    const fragment1 = new PlanLegFragment(null, 1000, List());
    const fragment2 = new PlanLegFragment(null, 2000, List());
    const fragment3 = new PlanLegFragment(null, 4000, List());

    const fragments1: List<PlanLegFragment> = List([fragment1, fragment2]);
    const fragments2: List<PlanLegFragment> = List([fragment3]);

    const leg1 = new PlanLeg("1", null, null, fragments1);
    const leg2 = new PlanLeg("2", null, null, fragments2);

    const plan = new Plan(null, List([leg1, leg2]));


    expect(plan.cumulativeKmLeg(0)).toEqual("3 km");
    expect(plan.cumulativeKmLeg(1)).toEqual("7 km");
  });

});
