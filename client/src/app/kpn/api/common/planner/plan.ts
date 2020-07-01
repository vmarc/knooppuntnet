import {List} from "immutable";
import {PlanNode} from "./plan-node";
import {PlanLeg} from "./plan-leg";

export class Plan {

  constructor(readonly sourceNode: PlanNode,
              readonly legs: List<PlanLeg>) {
  }

}
