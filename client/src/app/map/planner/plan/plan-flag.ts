import { Coordinate } from 'ol/coordinate';
import { PlanFlagType } from './plan-flag-type';

export class PlanFlag {
  constructor(
    readonly flagType: PlanFlagType,
    readonly featureId: string,
    readonly coordinate: Coordinate
  ) {}

  static start(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.Start, featureId, coordinate);
  }

  static end(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.End, featureId, coordinate);
  }

  static via(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.Via, featureId, coordinate);
  }

  static invisible(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.Invisible, featureId, coordinate);
  }

  toVia(): PlanFlag {
    return this.to(PlanFlagType.Via);
  }

  toEnd(): PlanFlag {
    return this.to(PlanFlagType.End);
  }

  toInvisible(): PlanFlag {
    return this.to(PlanFlagType.Invisible);
  }

  to(planFlagType: PlanFlagType): PlanFlag {
    return new PlanFlag(planFlagType, this.featureId, this.coordinate);
  }

  withCoordinate(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(this.flagType, this.featureId, coordinate);
  }
}
