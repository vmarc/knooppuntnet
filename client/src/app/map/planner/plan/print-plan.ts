import {List} from "immutable";
import {ViaRoute} from "../../../kpn/api/common/planner/via-route";
import {Plan} from "./plan";
import {PlanLeg} from "./plan-leg";
import {PlanNode} from "./plan-node";

class PrintPlanNode {

  private constructor(readonly featureId: string,
                      readonly nodeName: string) {
  }

  static from(planNode: PlanNode): PrintPlanNode {
    if (planNode === null) {
      return null;
    }
    return new PrintPlanNode(planNode.featureId, planNode.nodeName);
  }
}

class PrintPlanLeg {

  constructor(readonly featureId: string,
              readonly source: PrintPlanNode,
              readonly sink: PrintPlanNode,
              readonly viaRoute: ViaRoute) {
  }

  static from(leg: PlanLeg): PrintPlanLeg {
    return new PrintPlanLeg(
      leg.featureId,
      PrintPlanNode.from(leg.source),
      PrintPlanNode.from(leg.sink),
      leg.viaRoute
    );
  }
}

export class PrintPlan {

  constructor(readonly  source: PrintPlanNode,
              readonly  legs: List<PrintPlanLeg>) {
  }

  static from(plan: Plan): PrintPlan {
    return new PrintPlan(
      PrintPlanNode.from(plan.source),
      plan.legs.map(leg => PrintPlanLeg.from(leg))
    );
  }
}
