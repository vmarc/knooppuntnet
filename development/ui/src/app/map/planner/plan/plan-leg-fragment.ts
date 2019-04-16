import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {PlanNode} from "./plan-node";

export class PlanLegFragment {
  constructor(public readonly sink: PlanNode,
              public readonly meters: number,
              public readonly coordinates: List<Coordinate>) {
  }
}
