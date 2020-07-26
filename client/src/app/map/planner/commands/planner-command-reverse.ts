import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandReverse implements PlannerCommand {

  constructor(private readonly oldPlan: Plan,
              private readonly newPlan: Plan) {
  }

  public do(context: PlannerContext) {
    context.debug("PlannerCommandReverse");
    this.updatePlan(context, this.oldPlan, this.newPlan);
  }

  public undo(context: PlannerContext) {
    context.debug("PlannerCommandReverse undo");
    this.updatePlan(context, this.newPlan, this.oldPlan);
  }

  private updatePlan(context: PlannerContext, fromPlan: Plan, toPlan: Plan) {
    context.routeLayer.removePlan(fromPlan);
    context.markerLayer.removePlan(fromPlan);
    context.routeLayer.addPlan(toPlan);
    context.markerLayer.addPlan(toPlan);
  }

}
