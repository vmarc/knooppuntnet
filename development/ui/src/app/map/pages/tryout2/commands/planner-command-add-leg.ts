import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerContext} from "../planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddLeg implements PlannerCommand {

  constructor(private leg: PlanLeg) {
  }

  public do(context: PlannerContext) {
    context.addViaNodeFlag(this.leg.legId, this.leg.sink.nodeId, this.leg.sink.coordinate);
    context.addRouteLeg(this.leg.legId);
    const newLegs = context.plan().legs.push(this.leg);
    const newPlan = new Plan(context.plan().source, newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {
    const plan = context.plan();
    const newLegs = plan.legs.slice(0, -1);
    const newPlan = new Plan(plan.source, newLegs);
    context.updatePlan(newPlan);
    context.removeRouteLeg(this.leg.legId);
    context.removeViaNodeFlag(this.leg.legId, this.leg.sink.nodeId);
  }

}
