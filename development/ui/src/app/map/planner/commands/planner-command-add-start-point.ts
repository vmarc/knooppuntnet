import {List} from "immutable";
import {Plan} from "../plan/plan";
import {PlanNode} from "../plan/plan-node";
import {PlannerContext} from "../interaction/planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddStartPoint implements PlannerCommand {

  constructor(private node: PlanNode) {
  }

  public do(context: PlannerContext) {
    const plan = new Plan(this.node, List());
    context.addStartNodeFlag(this.node.nodeId, this.node.coordinate);
    context.updatePlan(plan);
  }

  public undo(context: PlannerContext) {
    const plan = Plan.empty();
    context.updatePlan(plan);
    context.removeStartNodeFlag(this.node.nodeId);
  }

}
