import {PlannerCommand} from "./planner-command";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerContext} from "../planner-context";
import {Plan} from "../plan/plan";

export class PlannerCommandSplitLeg implements PlannerCommand {

  constructor(private oldLeg: PlanLeg,
              private newLeg1: PlanLeg,
              private newLeg2: PlanLeg) {
  }

  public do(context: PlannerContext) {
    const plan: Plan = context.tempPlan();
    context.addViaNodeFlag(this.newLeg1.legId, this.newLeg1.sink.nodeId, this.newLeg1.sink.coordinate);
    const legIndex = plan.legs.findIndex(leg => leg.legId === this.oldLeg.legId);
    if (legIndex > -1) {
      context.removeRouteLeg(this.oldLeg.legId);
      if (!this.newLeg1.fragments.isEmpty()) {
        context.addRouteLeg(this.newLeg1.legId, this.newLeg1.fragments.flatMap(f => f.coordinates));
      }
      if (!this.newLeg2.fragments.isEmpty()) {
        context.addRouteLeg(this.newLeg2.legId, this.newLeg2.fragments.flatMap(f => f.coordinates));
      }
      const newLegs = plan.legs.remove(legIndex).push(this.newLeg1).push(this.newLeg2);
      const newPlan = new Plan(plan.source, newLegs);
      context.updatePlan(newPlan);
    }
  }

  public undo(context: PlannerContext) {
    context.removeViaNodeFlag(this.newLeg1.legId, this.newLeg1.sink.nodeId); // remove connection node
    context.removeRouteLeg(this.newLeg1.legId);
    context.removeRouteLeg(this.newLeg2.legId);

    const legIndex = context.plan.legs.findIndex(leg => leg.legId === this.newLeg1.legId);
    if (legIndex > -1) {
      const newLegs = context.plan.legs.remove(legIndex).remove(legIndex).insert(legIndex, this.oldLeg);
      const newPlan = new Plan(context.plan.source, newLegs);
      context.updatePlan(newPlan);
    }
  }

}
