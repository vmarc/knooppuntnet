import {Coordinate} from "ol/coordinate";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlanFlagType} from "./plan-flag-type";

export class PlanFlag {

  constructor(readonly flagType: PlanFlagType,
              readonly featureId: string,
              readonly coordinate: Coordinate) {
  }

  static oldStart(node: PlanNode): PlanFlag {
    return new PlanFlag(PlanFlagType.Start, node.featureId, node.coordinate);
  }

  static oldEnd(node: PlanNode): PlanFlag {
    return new PlanFlag(PlanFlagType.End, node.featureId, node.coordinate);
  }

  static oldVia(node: PlanNode): PlanFlag {
    return new PlanFlag(PlanFlagType.Via, node.featureId, node.coordinate);
  }

  static start(featureId: string, node: PlanNode): PlanFlag {
    return new PlanFlag(PlanFlagType.Start, featureId, node.coordinate);
  }

  static end(featureId: string, node: PlanNode): PlanFlag {
    return new PlanFlag(PlanFlagType.End, featureId, node.coordinate);
  }

  static via(featureId: string, node: PlanNode): PlanFlag {
    return new PlanFlag(PlanFlagType.Via, featureId, node.coordinate);
  }

  toVia(): PlanFlag {
    return new PlanFlag(PlanFlagType.Via, this.featureId, this.coordinate);
  }

  toEnd(): PlanFlag {
    return new PlanFlag(PlanFlagType.End, this.featureId, this.coordinate);
  }

  toInvisible(): PlanFlag {
    return new PlanFlag(PlanFlagType.Invisble, this.featureId, this.coordinate);
  }

  withCoordinate(coordinate: Coordinate): PlanFlag {
    return new PlanFlag(this.flagType, this.featureId, coordinate);
  }

}
