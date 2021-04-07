import { Coordinate } from 'ol/coordinate';
import { PlanFlagType } from './plan-flag-type';

export class PlanFlag {
  constructor(
    readonly flagType: PlanFlagType,
    readonly featureId: string,
    readonly coordinate: Coordinate
  ) {}

  static start(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.start, featureId, coordinate);
  }

  static end(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.end, featureId, coordinate);
  }

  static via(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.via, featureId, coordinate);
  }

  static invisible(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.invisible, featureId, coordinate);
  }

  toVia(): PlanFlag {
    return this.to(PlanFlagType.via);
  }

  toEnd(): PlanFlag {
    return this.to(PlanFlagType.end);
  }

  toInvisible(): PlanFlag {
    return this.to(PlanFlagType.invisible);
  }

  to(planFlagType: PlanFlagType): PlanFlag {
    return new PlanFlag(planFlagType, this.featureId, this.coordinate);
  }

  withCoordinate(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(this.flagType, this.featureId, coordinate);
  }
}
