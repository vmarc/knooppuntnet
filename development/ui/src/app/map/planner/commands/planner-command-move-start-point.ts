import {List} from "immutable";
import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveStartPoint implements PlannerCommand {

  constructor(private readonly oldNode: PlanNode,
              private readonly newNode: PlanNode) {
  }

  public do(context: PlannerContext) {
    this.update(context, this.oldNode, this.newNode);
  }

  public undo(context: PlannerContext) {
    this.update(context, this.newNode, this.oldNode);
  }

  public update(context: PlannerContext, fromNode: PlanNode, toNode: PlanNode) {
    context.routeLayer.removeFlag(fromNode.featureId);
    context.routeLayer.addFlag(PlanFlag.fromStartNode(toNode));
    const newPlan = Plan.create(toNode, List());
    context.updatePlan(newPlan);
  }

}
