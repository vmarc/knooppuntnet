import {PlanLeg} from "./plan-leg";
import {PlannerDragNode} from "./planner-drag-node";
import {Plan} from "./plan";

export class PlannerDragNodeAnalyzer {

  constructor(private plan: Plan) {
  }

  dragStarted(legNodeId: string, nodeId: string): PlannerDragNode {

    const legs = this.plan.legs;
    if (legs.isEmpty()) {
      return null;
    }

    if (legNodeId.startsWith("start-node-flag-")) {
      const firstLeg: PlanLeg = legs.first();
      const anchor = firstLeg.source.coordinate;
      return new PlannerDragNode(anchor, anchor, firstLeg.source);
    } else {
      const legIndex = legs.findIndex(leg => leg.source.nodeId === nodeId);
      if (legIndex > 0) {
        const previousLeg = legs.get(legIndex - 1);
        const nextLeg = legs.get(legIndex);
        const anchor1 = previousLeg.source.coordinate;
        const anchor2 = nextLeg.sink.coordinate;
        return new PlannerDragNode(anchor1, anchor2, nextLeg.source);
      }
      const lastLeg: PlanLeg = legs.last();
      if (lastLeg.sink.nodeId === nodeId) {
        const anchor = lastLeg.sink.coordinate;
        return new PlannerDragNode(anchor, anchor, lastLeg.sink);
      }
    }
    return null;
  }

}
