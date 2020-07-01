// this class is generated, please do not modify

import {List} from "immutable";
import {PlanNode} from "./plan-node";
import {PlanSegment} from "./plan-segment";

export class PlanRoute {

  constructor(readonly sourceNode: PlanNode,
              readonly sinkNode: PlanNode,
              readonly meters: number,
              readonly segments: List<PlanSegment>,
              readonly streets: List<string>) {
  }

  public static fromJSON(jsonObject: any): PlanRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanRoute(
      PlanNode.fromJSON(jsonObject.sourceNode),
      PlanNode.fromJSON(jsonObject.sinkNode),
      jsonObject.meters,
      jsonObject.segments ? List(jsonObject.segments.map((json: any) => PlanSegment.fromJSON(json))) : List(),
      jsonObject.streets ? List(jsonObject.streets) : List()
    );
  }
}
