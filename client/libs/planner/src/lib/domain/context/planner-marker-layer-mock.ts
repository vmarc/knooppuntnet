import { expect } from '@jest/globals';
import { Map as PlanFlagMap } from 'immutable';
import { Coordinate } from 'ol/coordinate';
import { expectCoordinate } from '../../util/test-support';
import { PlanFlag } from '../plan/plan-flag';
import { PlanFlagType } from '../plan/plan-flag-type';
import { PlannerMarkerLayer } from './planner-marker-layer';

export class PlannerMarkerLayerMock extends PlannerMarkerLayer {
  private flags: PlanFlagMap<string, PlanFlag> = PlanFlagMap<
    string,
    PlanFlag
  >();

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
      this.flags = this.flags.set(
        featureId,
        new PlanFlag(oldFlag.flagType, featureId, coordinate)
      );
    }
  }

  expectFlagCount(count: number): void {
    if (this.flags.size !== count) {
      let message = `the expected number of route flags (${count}) does not match the actual (${this.flags.size}) number of flags`;
      if (!this.flags.isEmpty()) {
        message += `, the route-layer contains following flag(s): `;
        message += this.flags.map((flag) => `"${flag.featureId}"`).join(', ');
      }
      throw new Error(message);
    }
  }

  expectStartFlagExists(featureId: string, coordinate: Coordinate): void {
    this.expectFlagExists(PlanFlagType.start, featureId, coordinate);
  }

  expectViaFlagExists(featureId: string, coordinate: Coordinate): void {
    this.expectFlagExists(PlanFlagType.via, featureId, coordinate);
  }

  expectEndFlagExists(featureId: string, coordinate: Coordinate): void {
    this.expectFlagExists(PlanFlagType.end, featureId, coordinate);
  }

  expectInvisibleFlagExists(featureId: string, coordinate: Coordinate): void {
    this.expectFlagExists(PlanFlagType.invisible, featureId, coordinate);
  }

  expectFlagExists(
    flagType: PlanFlagType,
    featureId: string,
    coordinate: Coordinate
  ): void {
    const flag = this.flags.get(featureId);
    if (!flag) {
      let message = `Cannot find flag with featureId "${featureId}"`;
      if (this.flags.isEmpty()) {
        message += ', no flags in route-layer';
      } else {
        message += ', route-layer contains following flags:';
        this.flags.forEach((feature, key) => {
          message += `\n  featureId="${feature.featureId}", type=`;
          if (feature.flagType === PlanFlagType.start) {
            message += 'Start';
          } else if (feature.flagType === PlanFlagType.via) {
            message += 'Via';
          } else if (feature.flagType === PlanFlagType.end) {
            message += 'End';
          } else if (feature.flagType === PlanFlagType.invisible) {
            message += 'Invisible';
          }
          message += `, coordinate=[${feature.coordinate[0]}, ${feature.coordinate[0]}]`;
        });
      }
      throw new Error(message);
    }

    expect(flag.flagType).toEqual(flagType);
    expectCoordinate(flag.coordinate, coordinate);
  }

  expectStartFlagCoordinateExists(coordinate: Coordinate): void {
    this.expectFlagCoordinateExists(PlanFlagType.start, coordinate);
  }

  expectViaFlagCoordinateExists(coordinate: Coordinate): void {
    this.expectFlagCoordinateExists(PlanFlagType.via, coordinate);
  }

  expectEndFlagCoordinateExists(coordinate: Coordinate): void {
    this.expectFlagCoordinateExists(PlanFlagType.end, coordinate);
  }

  expectInvisibleCoordinateFlagExists(coordinate: Coordinate): void {
    this.expectFlagCoordinateExists(PlanFlagType.invisible, coordinate);
  }

  expectFlagCoordinateExists(
    flagType: PlanFlagType,
    coordinate: Coordinate
  ): void {
    const coordinateString = JSON.stringify(coordinate);
    const result = this.flags
      .valueSeq()
      .find(
        (flag) =>
          flag.flagType === flagType &&
          JSON.stringify(flag.coordinate) === coordinateString
      );
    if (!result) {
      throw new Error(
        `could not find flag with type ${flagType} and coordinate ${coordinate}`
      );
    }
  }

  expectPlanFlagExists(planFlag: PlanFlag): void {
    const flag = this.flags.get(planFlag.featureId);
    expect(flag.featureId).toEqual(planFlag.featureId);
    expect(flag.flagType).toEqual(planFlag.flagType);
    expectCoordinate(flag.coordinate, planFlag.coordinate);
  }
}
