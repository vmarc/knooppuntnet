import {List} from "immutable";
import {PlanNode} from "./plan-node";
import {PlanLegFragment} from "./plan-leg-fragment";

export class PlanLeg {
  
  constructor(public readonly legId: string,
              public readonly source: PlanNode,
              public readonly sink: PlanNode,
              public readonly fragments: List<PlanLegFragment>) {
  }
  
}
