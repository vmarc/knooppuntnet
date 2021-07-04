import { Coordinate } from 'ol/coordinate';
import { PlanFlag } from '../map/planner/plan/plan-flag';
import { PlanFlagType } from '../map/planner/plan/plan-flag-type';
import { PlanLeg } from '../map/planner/plan/plan-leg';
import { PlanUtil } from '../map/planner/plan/plan-util';

export function expectCoordinate(
  actual: Coordinate,
  expected: Coordinate
): void {
  expect(JSON.stringify(actual)).toEqual(JSON.stringify(expected));
}

export function expectCoordinates(
  planLeg: PlanLeg,
  ...expected: Array<Coordinate>
): void {
  const actual = planLeg.routes.flatMap((route) =>
    PlanUtil.planRouteCoordinates(route)
  );
  expect(JSON.stringify(actual)).toEqual(JSON.stringify(expected));
}

export function expectStartFlag(
  planFlag: PlanFlag,
  featureId: string,
  coordinate
) {
  expectFlag(planFlag, PlanFlagType.Start, featureId, coordinate);
}

export function expectViaFlag(
  planFlag: PlanFlag,
  featureId: string,
  coordinate
) {
  expectFlag(planFlag, PlanFlagType.Via, featureId, coordinate);
}

export function expectEndFlag(
  planFlag: PlanFlag,
  featureId: string,
  coordinate
) {
  expectFlag(planFlag, PlanFlagType.End, featureId, coordinate);
}

export function expectInvisibleFlag(
  planFlag: PlanFlag,
  featureId: string,
  coordinate
) {
  expectFlag(planFlag, PlanFlagType.Invisible, featureId, coordinate);
}

export function expectStartFlagCoordinate(planFlag: PlanFlag, coordinate) {
  expectFlagCoordinate(planFlag, PlanFlagType.Start, coordinate);
}

export function expectViaFlagCoordinate(planFlag: PlanFlag, coordinate) {
  expectFlagCoordinate(planFlag, PlanFlagType.Via, coordinate);
}

export function expectEndFlagCoordinate(planFlag: PlanFlag, coordinate) {
  expectFlagCoordinate(planFlag, PlanFlagType.End, coordinate);
}

export function expectInvisibleFlagCoordinate(planFlag: PlanFlag, coordinate) {
  expectFlagCoordinate(planFlag, PlanFlagType.Invisible, coordinate);
}

export function expectFlag(
  planFlag: PlanFlag,
  flagType: PlanFlagType,
  featureId: string,
  coordinate
) {
  expect(planFlag.flagType).toEqual(flagType);
  expect(planFlag.featureId).toEqual(featureId);
  expectCoordinate(planFlag.coordinate, coordinate);
}

export function expectFlagCoordinate(
  planFlag: PlanFlag,
  flagType: PlanFlagType,
  coordinate
) {
  expect(planFlag.flagType).toEqual(flagType);
  expectCoordinate(planFlag.coordinate, coordinate);
}
