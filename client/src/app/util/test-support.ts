import { Coordinate } from 'ol/coordinate';
import { PlanFlag } from '../map/planner/plan/plan-flag';
import { PlanFlagType } from '../map/planner/plan/plan-flag-type';
import { PlanLeg } from '../map/planner/plan/plan-leg';
import { PlanUtil } from '../map/planner/plan/plan-util';

export const expectCoordinate = (
  actual: Coordinate,
  expected: Coordinate
): void => {
  expect(JSON.stringify(actual)).toEqual(JSON.stringify(expected));
};

export const expectCoordinates = (
  planLeg: PlanLeg,
  ...expected: Array<Coordinate>
): void => {
  const actual = planLeg.routes.flatMap((route) =>
    PlanUtil.planRouteCoordinates(route)
  );
  expect(JSON.stringify(actual)).toEqual(JSON.stringify(expected));
};

export const expectStartFlag = (
  planFlag: PlanFlag,
  featureId: string,
  coordinate
) => {
  expectFlag(planFlag, PlanFlagType.start, featureId, coordinate);
};

export const expectViaFlag = (
  planFlag: PlanFlag,
  featureId: string,
  coordinate
) => {
  expectFlag(planFlag, PlanFlagType.via, featureId, coordinate);
};

export const expectEndFlag = (
  planFlag: PlanFlag,
  featureId: string,
  coordinate
) => {
  expectFlag(planFlag, PlanFlagType.end, featureId, coordinate);
};

export const expectInvisibleFlag = (
  planFlag: PlanFlag,
  featureId: string,
  coordinate
) => {
  expectFlag(planFlag, PlanFlagType.invisible, featureId, coordinate);
};

export const expectFlagCoordinate = (
  planFlag: PlanFlag,
  flagType: PlanFlagType,
  coordinate
) => {
  expect(planFlag.flagType).toEqual(flagType);
  expectCoordinate(planFlag.coordinate, coordinate);
};

export const expectStartFlagCoordinate = (planFlag: PlanFlag, coordinate) => {
  expectFlagCoordinate(planFlag, PlanFlagType.start, coordinate);
};

export const expectViaFlagCoordinate = (planFlag: PlanFlag, coordinate) => {
  expectFlagCoordinate(planFlag, PlanFlagType.via, coordinate);
};

export const expectEndFlagCoordinate = (planFlag: PlanFlag, coordinate) => {
  expectFlagCoordinate(planFlag, PlanFlagType.end, coordinate);
};

export const expectInvisibleFlagCoordinate = (
  planFlag: PlanFlag,
  coordinate
) => {
  expectFlagCoordinate(planFlag, PlanFlagType.invisible, coordinate);
};

export const expectFlag = (
  planFlag: PlanFlag,
  flagType: PlanFlagType,
  featureId: string,
  coordinate
) => {
  expect(planFlag.flagType).toEqual(flagType);
  expect(planFlag.featureId).toEqual(featureId);
  expectCoordinate(planFlag.coordinate, coordinate);
};
