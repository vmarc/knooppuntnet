import {Plan} from "./plan/plan";
import {PlanLeg} from "./plan/plan-leg";
import {PlannerDragNode} from "./planner-drag-node";

export class PlannerDragNodeAnalyzer {

  constructor(private plan: Plan) {
  }

  dragStarted(legNodeFeatureId: string, nodeId: string): PlannerDragNode {

    const legs = this.plan.legs;
    if (legs.isEmpty()) {
      return null;
    }

    if (legNodeFeatureId.startsWith("start-node-flag-")) {
      const firstLeg: PlanLeg = legs.first();
      const anchor = firstLeg.source.coordinate;
      return new PlannerDragNode(legNodeFeatureId, anchor, anchor, firstLeg.source);
    }

    const lastLeg: PlanLeg = legs.last();
    if (lastLeg.sink.nodeId == nodeId) {
      const anchor = lastLeg.sink.coordinate;
      return new PlannerDragNode(legNodeFeatureId, anchor, anchor, lastLeg.sink);
    }

    const legIndex = legs.findIndex(leg => leg.source.nodeId === nodeId);
    if (legIndex > 0) {
      const previousLeg = legs.get(legIndex - 1);
      const nextLeg = legs.get(legIndex);
      const anchor1 = previousLeg.source.coordinate;
      const anchor2 = nextLeg.sink.coordinate;
      return new PlannerDragNode(legNodeFeatureId, anchor1, anchor2, nextLeg.source);
    }

    return null;
  }

}
