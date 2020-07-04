import {Coordinate} from "ol/coordinate";
import {PlanFlag} from "../map/planner/plan/plan-flag";
import {PlanFlagType} from "../map/planner/plan/plan-flag-type";
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

  static expectViaFlag(planFlag: PlanFlag, featureId: string, coordinate) {
    TestSupport.expectFlag(planFlag, PlanFlagType.Via, featureId, coordinate);
  }

  static expectStartFlag(planFlag: PlanFlag, featureId: string, coordinate) {
    TestSupport.expectFlag(planFlag, PlanFlagType.Start, featureId, coordinate);
  }

  static expectEndFlag(planFlag: PlanFlag, featureId: string, coordinate) {
    TestSupport.expectFlag(planFlag, PlanFlagType.End, featureId, coordinate);
  }

  static expectFlag(planFlag: PlanFlag, flagType: PlanFlagType, featureId: string, coordinate) {
    expect(planFlag.flagType).toEqual(flagType);
    expect(planFlag.featureId).toEqual(featureId);
    TestSupport.expectCoordinate(planFlag.coordinate, coordinate);
  }

}
