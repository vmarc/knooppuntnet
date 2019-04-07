import {PlannerContext} from "../interaction/planner-context";
import {Plan} from "../plan/plan";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddLeg implements PlannerCommand {

  constructor(private legId: string) {
  }

  public do(context: PlannerContext) {
    const leg = context.legCache().getById(this.legId);
    context.addViaNodeFlag(leg.legId, leg.sink.nodeId, leg.sink.coordinate);
    context.addRouteLeg(this.legId);
    const newLegs = context.plan().legs.push(leg);
    const newPlan = new Plan(context.plan().source, newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {
    const leg = context.legCache().getById(this.legId);
    const plan = context.plan();
    const newLegs = plan.legs.slice(0, -1);
    const newPlan = new Plan(plan.source, newLegs);
    context.updatePlan(newPlan);
    context.removeRouteLeg(this.legId);
    context.removeViaNodeFlag(this.legId, leg.sink.nodeId);
  }

}
