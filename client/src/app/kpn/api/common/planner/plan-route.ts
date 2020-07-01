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

}
