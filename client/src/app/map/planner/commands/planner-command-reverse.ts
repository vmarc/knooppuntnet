import {PlannerContext} from "../context/planner-context";
import {FeatureId} from "../features/feature-id";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandReverse implements PlannerCommand {

  private oldPlan: Plan;
  private newPlan: Plan;

  public do(context: PlannerContext) {
    this.oldPlan = context.plan;
    const newLegs = this.oldPlan.legs.reverse().map(leg => context.buildLeg(FeatureId.next(), leg.sink, leg.source));
    this.newPlan = Plan.create(newLegs.get(0).source, newLegs);
    this.removeFlagsAndLegs(context, this.oldPlan);
    this.addFlagsAndLegs(context, this.newPlan);
    context.updatePlan(this.newPlan);
  }

  public undo(context: PlannerContext) {
    this.removeFlagsAndLegs(context, this.newPlan);
    this.addFlagsAndLegs(context, this.oldPlan);
    context.updatePlan(this.oldPlan);
  }

  private removeFlagsAndLegs(context: PlannerContext, plan: Plan): void {
    context.routeLayer.removeFlag(plan.source.featureId);
    plan.legs.forEach(leg => {
      context.routeLayer.removeRouteLeg(leg.featureId);
      context.routeLayer.removeFlag(leg.sink.featureId);
    });
  }

  private addFlagsAndLegs(context: PlannerContext, plan: Plan): void {
    context.routeLayer.addFlag(PlanFlag.fromStartNode(plan.source));
    for (let i = 0; i < plan.legs.size; i++) {
      const leg = plan.legs.get(i);
      context.routeLayer.addRouteLeg(leg);
      if (i < plan.legs.size - 1) {
        context.routeLayer.addFlag(PlanFlag.fromViaNode(leg.sink));
      } else {
        context.routeLayer.addFlag(PlanFlag.fromEndNode(leg.sink));
      }
    }
  }
}
