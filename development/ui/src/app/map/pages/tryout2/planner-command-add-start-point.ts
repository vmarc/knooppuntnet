import {PlannerCommand} from "./planner-command";
import {PlannerContext} from "./planner-context";
import {Plan} from "./plan";
import {PlanNode} from "./plan-node";
import {List} from "immutable";

export class PlannerCommandAddStartPoint implements PlannerCommand {

  constructor(private node: PlanNode) {
  }

  public do(context: PlannerContext) {
    const plan = new Plan(this.node, List());
    context.updatePlan(plan);
    context.routeLayer.addStartNodeFlag(this.node.nodeId, this.node.coordinate);
  }

  public undo(context: PlannerContext) {
    const plan = new Plan(null, List());
    context.updatePlan(plan);
    context.routeLayer.removeStartNodeFlag(this.node.nodeId);
  }

}
