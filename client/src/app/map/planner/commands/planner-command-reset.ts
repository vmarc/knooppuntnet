import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandReset implements PlannerCommand {

  private oldPlan: Plan;

  public do(context: PlannerContext) {
    this.oldPlan = context.plan;
    context.routeLayer.removePlan(this.oldPlan);
    context.updatePlan(Plan.empty());
  }

  public undo(context: PlannerContext) {
    context.routeLayer.addPlan(this.oldPlan);
    context.updatePlan(this.oldPlan);
  }

}
