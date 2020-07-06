import {Map} from "immutable";
import {Coordinate} from "ol/coordinate";
import {TestSupport} from "../../../util/test-support";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerRouteLayerBase} from "./planner-route-layer-base";

export class PlannerRouteLayerMock extends PlannerRouteLayerBase {

  private flags: Map<string, PlanFlag> = Map();
  private routeLegs: Map<string, PlanLeg> = Map();

  addFlag(flag: PlanFlag): void {
    if (flag !== null) {
      this.flags = this.flags.set(flag.featureId, flag);
    }
  }

  updateFlag(flag: PlanFlag): void {
    if (flag !== null) {
      this.flags = this.flags.set(flag.featureId, flag);
    }
  }

  removeFlag(flag: PlanFlag): void {
    if (flag !== null) {
      this.flags = this.flags.remove(flag.featureId);
    }
  }

  removeFlagWithFeatureId(featureId: string): void {
    this.flags = this.flags.remove(featureId);
  }

  updateFlagCoordinate(featureId: string, coordinate: Coordinate): void {
    const oldFlag = this.flags.get(featureId);
    if (oldFlag) {
      this.flags = this.flags.set(featureId, new PlanFlag(oldFlag.flagType, featureId, coordinate));
    }
  }

  expectFlagCount(count: number): void {
    expect(this.flags.size).toEqual(count);
  }

  expectStartFlagExists(featureId: string, coordinate: Coordinate): void {
    this.expectFlagExists(PlanFlagType.Start, featureId, coordinate);
  }

  expectViaFlagExists(featureId: string, coordinate: Coordinate): void {
    this.expectFlagExists(PlanFlagType.Via, featureId, coordinate);
  }

  expectEndFlagExists(featureId: string, coordinate: Coordinate): void {
    this.expectFlagExists(PlanFlagType.End, featureId, coordinate);
  }

  expectFlagExists(flagType: PlanFlagType, featureId: string, coordinate: Coordinate): void {
    const flag = this.flags.get(featureId);
    expect(flag.featureId).toEqual(featureId);
    expect(flag.flagType).toEqual(flagType);
    TestSupport.expectCoordinate(flag.coordinate, coordinate);
  }

  expectPlanFlagExists(planFlag: PlanFlag): void {
    const flag = this.flags.get(planFlag.featureId);
    expect(flag.featureId).toEqual(planFlag.featureId);
    expect(flag.flagType).toEqual(planFlag.flagType);
    TestSupport.expectCoordinate(flag.coordinate, planFlag.coordinate);
  }

  addPlanLeg(leg: PlanLeg): void {
    this.routeLegs = this.routeLegs.set(leg.featureId, leg);
  }

  removePlanLeg(legId: string): void {
    this.routeLegs = this.routeLegs.remove(legId);
  }

  expectRouteLegCount(count: number): void {
    expect(this.routeLegs.size).toEqual(count);
  }

  expectRouteLegExists(legId: string, leg: PlanLeg): void {
    const routeLeg = this.routeLegs.get(legId);
    expect(routeLeg).toBeDefined();
    expect(routeLeg).toEqual(leg);
  }

}
