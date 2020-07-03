import {Coordinate} from "ol/coordinate";
import {PlanLeg} from "../map/planner/plan/plan-leg";
import {PlanUtil} from "../map/planner/plan/plan-util";

export class TestSupport {

  static expectCoordinate(actual: Coordinate, expected: Coordinate): void {
    expect(JSON.stringify(actual)).toEqual(JSON.stringify(expected));
  }

  static expectCoordinates(planLeg: PlanLeg, ...expected: Array<Coordinate>): void {
    const actual = planLeg.routes.flatMap(route => PlanUtil.planRouteCoordinates(route));
    expect(JSON.stringify(actual)).toEqual(JSON.stringify(expected));
  }

}
