import {PlannerContext} from "../context/planner-context";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddLeg implements PlannerCommand {

  constructor(private legId: string) {
  }

  public do(context: PlannerContext) {
    const leg = context.legs.getById(this.legId);
    const newLegs = context.plan.legs.push(leg);
    const newPlan = PlanUtil.plan(context.plan.sourceNode, newLegs);
    if (newLegs.size > 1) {
      context.routeLayer.removeFlag(leg.sourceNode.featureId);
      context.routeLayer.addFlag(PlanFlag.fromViaNode(leg.sourceNode));
    }
    context.routeLayer.addFlag(PlanFlag.fromEndNode(leg.sinkNode));
    context.routeLayer.addRouteLeg(leg);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {
    const leg = context.legs.getById(this.legId);
    const newLegs = context.plan.legs.slice(0, -1);
    const newPlan = PlanUtil.plan(context.plan.sourceNode, newLegs);
    context.updatePlan(newPlan);
    context.routeLayer.removeRouteLeg(this.legId);
    context.routeLayer.removeFlag(leg.sinkNode.featureId);
    if (!newLegs.isEmpty()) {
      context.routeLayer.removeFlag(leg.sourceNode.featureId);
      context.routeLayer.addFlag(PlanFlag.fromEndNode(leg.sourceNode));
    }
  }

}
