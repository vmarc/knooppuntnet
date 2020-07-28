import {Map} from "immutable";
import {Coordinate} from "ol/coordinate";
import {expectCoordinate} from "../../../util/test-support";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlannerMarkerLayer} from "./planner-marker-layer";

export class PlannerMarkerLayerMock extends PlannerMarkerLayer {

  private flags: Map<string, PlanFlag> = Map();

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

  expectInvisibleFlagExists(featureId: string, coordinate: Coordinate): void {
    this.expectFlagExists(PlanFlagType.Invisible, featureId, coordinate);
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
    expectCoordinate(flag.coordinate, coordinate);
  }

  expectStartFlagCoordinateExists(coordinate: Coordinate): void {
    this.expectFlagCoordinateExists(PlanFlagType.Start, coordinate);
  }

  expectViaFlagCoordinateExists(coordinate: Coordinate): void {
    this.expectFlagCoordinateExists(PlanFlagType.Via, coordinate);
  }

  expectEndFlagCoordinateExists(coordinate: Coordinate): void {
    this.expectFlagCoordinateExists(PlanFlagType.End, coordinate);
  }

  expectInvisibleCoordinateFlagExists(coordinate: Coordinate): void {
    this.expectFlagCoordinateExists(PlanFlagType.Invisible, coordinate);
  }

  expectFlagCoordinateExists(flagType: PlanFlagType, coordinate: Coordinate): void {
    const coordinateString = JSON.stringify(coordinate);
    const result = this.flags.valueSeq().find(flag => {
      return flag.flagType === flagType && JSON.stringify(flag.coordinate) === coordinateString;
    });
    if (!result) {
      fail(`could not find flag with type ${flagType} and coordinate ${coordinate}`);
    }
  }

  expectPlanFlagExists(planFlag: PlanFlag): void {
    const flag = this.flags.get(planFlag.featureId);
    expect(flag.featureId).toEqual(planFlag.featureId);
    expect(flag.flagType).toEqual(planFlag.flagType);
    expectCoordinate(flag.coordinate, planFlag.coordinate);
  }

}
