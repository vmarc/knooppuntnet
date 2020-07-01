// this class is generated, please do not modify

import {List} from "immutable";
import {LegEnd} from "./leg-end";
import {PlanNode} from "./plan-node";
import {PlanRoute} from "./plan-route";

export class PlanLeg {

  constructor(readonly featureId: string,
              readonly key: string,
              readonly source: LegEnd,
              readonly sink: LegEnd,
              readonly sourceNode: PlanNode,
              readonly sinkNode: PlanNode,
              readonly meters: number,
              readonly routes: List<PlanRoute>) {
  }

  public static fromJSON(jsonObject: any): PlanLeg {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanLeg(
      jsonObject.featureId,
      jsonObject.key,
      LegEnd.fromJSON(jsonObject.source),
      LegEnd.fromJSON(jsonObject.sink),
      PlanNode.fromJSON(jsonObject.sourceNode),
      PlanNode.fromJSON(jsonObject.sinkNode),
      jsonObject.meters,
      jsonObject.routes ? List(jsonObject.routes.map((json: any) => PlanRoute.fromJSON(json))) : List()
    );
  }
}
