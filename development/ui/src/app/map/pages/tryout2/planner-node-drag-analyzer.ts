import {PlanLeg} from "./plan-leg";
import {PlannerNodeDrag} from "./planner-node-drag";
import {Plan} from "./plan";

export class PlannerNodeDragAnalyzer {

  constructor(private plan: Plan) {
  }

  dragStarted(legNodeId: string, nodeId: string): PlannerNodeDrag {

    const legs = this.plan.legs;
    if (legs.isEmpty()) {
      return null;
    }

    if (legNodeId.startsWith("start-node-flag-")) {
      const firstLeg: PlanLeg = legs.first();
      const anchor = firstLeg.source.coordinate;
      return new PlannerNodeDrag(anchor, anchor, firstLeg.source);
    } else {
      const legIndex = legs.findIndex(leg => leg.source.nodeId === nodeId);
      if (legIndex > 0) {
        const previousLeg = legs.get(legIndex - 1);
        const nextLeg = legs.get(legIndex);
        const anchor1 = previousLeg.source.coordinate;
        const anchor2 = nextLeg.sink.coordinate;
        return new PlannerNodeDrag(anchor1, anchor2, nextLeg.source);
      }
      const lastLeg: PlanLeg = legs.last();
      if (lastLeg.sink.nodeId === nodeId) {
        const anchor = lastLeg.sink.coordinate;
        return new PlannerNodeDrag(anchor, anchor, lastLeg.sink);
      }
    }
    return null;
  }

}
