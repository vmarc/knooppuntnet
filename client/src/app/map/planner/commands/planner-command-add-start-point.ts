import {List} from "immutable";
import {Plan} from "../../../kpn/api/common/planner/plan";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlannerContext} from "../context/planner-context";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddStartPoint implements PlannerCommand {

  constructor(private node: PlanNode) {
  }

  public do(context: PlannerContext) {
    const plan = new Plan(this.node, List());
    context.routeLayer.addFlag(PlanFlag.fromStartNode(this.node));
    context.updatePlan(plan);
  }

  public undo(context: PlannerContext) {
    context.updatePlan(PlanUtil.emptyPlan);
    context.routeLayer.removeFlag(this.node.featureId);
  }

}
