import {PlannerContext} from "../interaction/planner-context";
import {Plan} from "../plan/plan";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandSplitLeg implements PlannerCommand {

  constructor(private oldLegId: string,
              private newLegId1: string,
              private newLegId2: string) {
  }

  public do(context: PlannerContext) {

    const oldLeg = context.legCache().getById(this.oldLegId);
    const newLeg1 = context.legCache().getById(this.newLegId1);
    const newLeg2 = context.legCache().getById(this.newLegId2);

    context.addViaNodeFlag(newLeg1.legId, newLeg1.sink.nodeId, newLeg1.sink.coordinate);
    const plan: Plan = context.plan();
    const legIndex = plan.legs.findIndex(leg => leg.legId === oldLeg.legId);
    if (legIndex > -1) {
      context.removeRouteLeg(oldLeg.legId);
      context.addRouteLeg(newLeg1.legId);
      context.addRouteLeg(newLeg2.legId);
      const newLegs = plan.legs.remove(legIndex).push(newLeg1).push(newLeg2);
      const newPlan = new Plan(plan.source, newLegs);
      context.updatePlan(newPlan);
    }
  }

  public undo(context: PlannerContext) {

    const oldLeg = context.legCache().getById(this.oldLegId);
    const newLeg1 = context.legCache().getById(this.newLegId1);
    const newLeg2 = context.legCache().getById(this.newLegId2);

    context.removeViaNodeFlag(newLeg1.legId, newLeg1.sink.nodeId); // remove connection node
    context.removeRouteLeg(newLeg1.legId);
    context.removeRouteLeg(newLeg2.legId);
    context.addRouteLeg(oldLeg.legId);
    const plan: Plan = context.plan();
    const legIndex = plan.legs.findIndex(leg => leg.legId === newLeg1.legId);
    if (legIndex > -1) {
      const newLegs = plan.legs.remove(legIndex).remove(legIndex).insert(legIndex, oldLeg);
      const newPlan = new Plan(plan.source, newLegs);
      context.updatePlan(newPlan);
    }
  }

}
