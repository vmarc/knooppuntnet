import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddLeg implements PlannerCommand {

  constructor(private legId: string) {
  }

  public do(context: PlannerContext) {
    const leg = context.legs.getById(this.legId);
    context.routeLayer.addFlag(PlanFlag.fromViaNode(leg.sink));
    context.routeLayer.addRouteLeg(leg);
    const newLegs = context.plan.legs.push(leg);
    const newPlan = Plan.create(context.plan.source, newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {
    const leg = context.legs.getById(this.legId);
    const newLegs = context.plan.legs.slice(0, -1);
    const newPlan = Plan.create(context.plan.source, newLegs);
    context.updatePlan(newPlan);
    context.routeLayer.removeRouteLeg(this.legId);
    context.routeLayer.removeFlag(leg.sink.featureId);
  }

}
