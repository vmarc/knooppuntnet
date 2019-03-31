import {List} from "immutable";
import {PlanNode} from "./plan-node";

export class PlanLegFragment {
  constructor(public readonly sink: PlanNode,
              public readonly meters: number,
              public readonly coordinates: List<Coordinates>) {
  }
}
