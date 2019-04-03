import {PlannerCommand} from "./planner-command";
import {List} from "immutable";
import {PlanNode} from "../plan/plan-node";
import {PlannerContext} from "../planner-context";
import {Plan} from "../plan/plan";

export class PlannerCommandAddStartPoint implements PlannerCommand {

  constructor(private node: PlanNode) {
  }

  public do(context: PlannerContext) {
    const plan = new Plan(this.node, List());
    context.updatePlan(plan);
    context.addStartNodeFlag(this.node.nodeId, this.node.coordinate);
  }

  public undo(context: PlannerContext) {
    const plan = Plan.empty();
    context.updatePlan(plan);
    context.removeStartNodeFlag(this.node.nodeId);
  }

}
