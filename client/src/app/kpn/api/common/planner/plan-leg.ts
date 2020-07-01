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

}
