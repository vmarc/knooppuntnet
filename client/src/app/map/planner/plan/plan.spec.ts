import {List} from "immutable";
import {Plan} from "./plan";
import {PlanLeg} from "./plan-leg";
import {PlanRoute} from "./plan-route";

describe("Plan", () => {

  it("total distance empty plan", () => {

    const plan = Plan.empty();

    expect(plan.cumulativeKmLeg(0)).toEqual("0 km");
  });

  it("total distance", () => {

    const route1 = new PlanRoute(null, null, 1000, List(), List());
    const route2 = new PlanRoute(null, null, 2000, List(), List());
    const route3 = new PlanRoute(null, null, 4000, List(), List());

    const leg1 = new PlanLeg("1", "", null, null, 3000, List([route1, route2]));
    const leg2 = new PlanLeg("2", "", null, null, 4000, List([route3]));

    const plan = Plan.create(null, List([leg1, leg2]));

    expect(plan.cumulativeKmLeg(0)).toEqual("3 km");
    expect(plan.cumulativeKmLeg(1)).toEqual("7 km");

  });

});
