import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandReverse implements PlannerCommand {

  constructor(private readonly oldPlan: Plan,
              private readonly newPlan: Plan) {
  }

  public do(context: PlannerContext) {
    context.debug("PlannerCommandReverse");
    context.routeLayer.removePlan(this.oldPlan);
    context.markerLayer.removePlan(this.oldPlan);
    this.updatePlan(context, this.newPlan);
  }

  public undo(context: PlannerContext) {
    context.debug("PlannerCommandReverse undo");
    context.routeLayer.removePlan(this.newPlan);
    context.markerLayer.removePlan(this.newPlan);
    this.updatePlan(context, this.oldPlan);
  }

  private updatePlan(context: PlannerContext, plan: Plan) {
    const updatedPlan = new Plan(
      plan.sourceNode,
      plan.sourceFlag,
      plan.legs.map(leg => {
        const updatedLeg = context.legs.get(leg.key);
        if (updatedLeg) {
          return updatedLeg;
        }
        return leg;
      })
    );
    context.routeLayer.addPlan(updatedPlan);
    context.markerLayer.addPlan(updatedPlan);
    context.updatePlan(updatedPlan);
  }

}
