import {PlannerContext} from "../context/planner-context";
import {FeatureId} from "../features/feature-id";
import {Plan} from "../plan/plan";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandReverse implements PlannerCommand {

  private oldPlan: Plan;
  private newPlan: Plan;

  public do(context: PlannerContext) {
    this.oldPlan = context.plan;
    const newLegs = this.oldPlan.legs.reverse().map(leg => context.oldBuildLeg(FeatureId.next(), leg.sinkNode, leg.sourceNode));
    this.newPlan = Plan.create(newLegs.get(0).sourceNode, newLegs);
    context.routeLayer.removePlan(this.oldPlan);
    context.routeLayer.addPlan(this.newPlan);
    context.updatePlan(this.newPlan);
  }

  public undo(context: PlannerContext) {
    context.routeLayer.removePlan(this.newPlan);
    context.routeLayer.addPlan(this.oldPlan);
    context.updatePlan(this.oldPlan);
  }

}
