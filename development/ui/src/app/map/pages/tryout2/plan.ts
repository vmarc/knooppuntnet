import {List} from "immutable";
import {PlanNode} from "./plan-node";
import {PlanLeg} from "./plan-leg";

export class Plan {

  constructor(public readonly source: PlanNode,
              public readonly legs: List<PlanLeg>) {
  }

  lastNode(): PlanNode {
    const lastLeg = this.legs.last(null);
    if (lastLeg) {
      return lastLeg.sink;
    }
    return this.source;
  }

}
