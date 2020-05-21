import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandReset implements PlannerCommand {

  private oldPlan: Plan;

  public do(context: PlannerContext) {
    this.oldPlan = context.plan;
    context.routeLayer.removeFlag(this.oldPlan.source.featureId);
    this.oldPlan.legs.forEach(leg => {
      context.routeLayer.removeRouteLeg(leg.featureId);
      context.routeLayer.removeFlag(leg.sink.featureId);
    });
    context.updatePlan(Plan.empty());
  }

  public undo(context: PlannerContext) {
    context.routeLayer.addFlag(PlanFlag.fromStartNode(this.oldPlan.source));
    for (let i = 0; i < this.oldPlan.legs.size; i++) {
      const leg = this.oldPlan.legs.get(i);
      context.routeLayer.addRouteLeg(leg);
      if (i < this.oldPlan.legs.size - 1) {
        context.routeLayer.addFlag(PlanFlag.fromViaNode(leg.sink));
      } else {
        context.routeLayer.addFlag(PlanFlag.fromEndNode(leg.sink));
      }
    }
    context.updatePlan(this.oldPlan);
  }

}
