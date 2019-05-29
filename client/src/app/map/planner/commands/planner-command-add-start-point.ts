import {List} from "immutable";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanNode} from "../plan/plan-node";
import {PlannerContext} from "../context/planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddStartPoint implements PlannerCommand {

  constructor(private node: PlanNode) {
  }

  public do(context: PlannerContext) {
    const plan = Plan.create(this.node, List());
    context.routeLayer.addFlag(PlanFlag.fromStartNode(this.node));
    context.updatePlan(plan);
  }

  public undo(context: PlannerContext) {
    const plan = Plan.empty();
    context.updatePlan(plan);
    context.routeLayer.removeFlag(this.node.featureId);
  }

}
