import {Coordinate} from "ol/coordinate";
import {PlanFlagType} from "./plan-flag-type";

export class PlanFlag {

  constructor(readonly flagType: PlanFlagType,
              readonly featureId: string,
              readonly coordinate: Coordinate) {
  }

  static start(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.Start, featureId, coordinate);
  }

  static end(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.End, featureId, coordinate);
  }

  static via(featureId: string, coordinate: Coordinate): PlanFlag {
    return new PlanFlag(PlanFlagType.Via, featureId, coordinate);
  }

  toVia(): PlanFlag {
    return new PlanFlag(PlanFlagType.Via, this.featureId, this.coordinate);
  }

  toEnd(): PlanFlag {
    return new PlanFlag(PlanFlagType.End, this.featureId, this.coordinate);
  }

  toInvisible(): PlanFlag {
    return new PlanFlag(PlanFlagType.Invisible, this.featureId, this.coordinate);
  }

  withCoordinate(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(this.flagType, this.featureId, coordinate);
  }

}
