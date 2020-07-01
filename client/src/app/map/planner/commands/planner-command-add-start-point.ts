import {List} from "immutable";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlannerContext} from "../context/planner-context";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddStartPoint implements PlannerCommand {

  constructor(private node: PlanNode) {
  }

  public do(context: PlannerContext) {
    const plan = PlanUtil.plan(this.node, List());
    context.routeLayer.addFlag(PlanFlag.fromStartNode(this.node));
    context.updatePlan(plan);
  }

  public undo(context: PlannerContext) {
    const plan = PlanUtil.planEmpty();
    context.updatePlan(plan);
    context.routeLayer.removeFlag(this.node.featureId);
  }

}
