// this class is generated, please do not modify

import {List} from "immutable";
import {PlanLeg} from "./plan-leg";
import {PlanNode} from "./plan-node";

export class Plan {

  constructor(readonly sourceNode: PlanNode,
              readonly legs: List<PlanLeg>) {
  }

  public static fromJSON(jsonObject: any): Plan {
    if (!jsonObject) {
      return undefined;
    }
    return new Plan(
      PlanNode.fromJSON(jsonObject.sourceNode),
      jsonObject.legs ? List(jsonObject.legs.map((json: any) => PlanLeg.fromJSON(json))) : List()
    );
  }
}
