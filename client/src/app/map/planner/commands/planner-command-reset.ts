import {Plan} from "../../../kpn/api/common/planner/plan";
import {PlannerContext} from "../context/planner-context";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandReset implements PlannerCommand {

  private oldPlan: Plan;

  public do(context: PlannerContext) {
    this.oldPlan = context.plan;
    context.routeLayer.removePlan(this.oldPlan);
    context.updatePlan(PlanUtil.planEmpty());
  }

  public undo(context: PlannerContext) {
    context.routeLayer.addPlan(this.oldPlan);
    context.updatePlan(this.oldPlan);
  }

}
