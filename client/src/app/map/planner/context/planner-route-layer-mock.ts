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
    if (this.flags.size !== count) {
      let message = `the expected number of route flags (${count}) does not match the actual (${this.flags.size}) number of flags`;
      if (!this.flags.isEmpty()) {
        message += `, the route-layer contains following flag(s): `;
        message += this.flags.map(flag => `"${flag.featureId}"`).join(", ");
      }
      fail(message);
    }
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
    if (!flag) {
      let message = `Cannot find flag with featureId "${featureId}"`;
      if (this.flags.isEmpty()) {
        message += ", no flags in route-layer";
      } else {
        message += ", route-layer contains following flags:";
        for (const feature of this.flags.values()) {
          message += `\n  featureId="${feature.featureId}", type=`;
          if (feature.flagType === PlanFlagType.Start) {
            message += "Start";
          } else if (feature.flagType === PlanFlagType.Via) {
            message += "Via";
          } else if (feature.flagType === PlanFlagType.End) {
            message += "End";
          } else if (feature.flagType === PlanFlagType.Invisible) {
            message += "Invisible";
          }
          message += `, coordinate=[${feature.coordinate[0]}, ${feature.coordinate[0]}]`;
        }
      }
      fail(message);
    }

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
    if (this.routeLegs.size !== count) {
      let message = `the expected number of route legs (${count}) does not match the actual (${this.routeLegs.size}) number of legs`;
      if (!this.routeLegs.isEmpty()) {
        message += `, the route-layer contains following leg(s): `;
        message += this.routeLegs.map(leg => `${leg.featureId}(${leg.key})`).join(", ");
      }
      fail(message);
    }
  }

  expectRouteLegExists(legId: string, leg: PlanLeg): void {
    const routeLeg = this.routeLegs.get(legId);
    expect(routeLeg).toBeDefined();
    expect(routeLeg).toEqual(leg);
  }

}
