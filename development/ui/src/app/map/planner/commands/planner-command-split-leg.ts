import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandSplitLeg implements PlannerCommand {

  constructor(private oldLegId: string,
              private newLegId1: string,
              private newLegId2: string) {
  }

  public do(context: PlannerContext) {

    const oldLeg = context.legs.getById(this.oldLegId);
    const newLeg1 = context.legs.getById(this.newLegId1);
    const newLeg2 = context.legs.getById(this.newLegId2);

    context.routeLayer.addViaNodeFlag(newLeg1.legId, newLeg1.sink.nodeId, newLeg1.sink.coordinate);
    const legIndex = context.plan.legs.findIndex(leg => leg.legId === oldLeg.legId);
    if (legIndex > -1) {
      context.routeLayer.removeRouteLeg(oldLeg.legId);
      context.routeLayer.addRouteLeg(newLeg1);
      context.routeLayer.addRouteLeg(newLeg2);
      const newLegs = context.plan.legs.remove(legIndex).push(newLeg1).push(newLeg2);
      const newPlan = new Plan(context.plan.source, newLegs);
      context.updatePlan(newPlan);
    }
  }

  public undo(context: PlannerContext) {

    const oldLeg = context.legs.getById(this.oldLegId);
    const newLeg1 = context.legs.getById(this.newLegId1);
    const newLeg2 = context.legs.getById(this.newLegId2);

    context.routeLayer.removeViaNodeFlag(newLeg1.legId, newLeg1.sink.nodeId); // remove connection node
    context.routeLayer.removeRouteLeg(newLeg1.legId);
    context.routeLayer.removeRouteLeg(newLeg2.legId);
    context.routeLayer.addRouteLeg(oldLeg);
    const legIndex = context.plan.legs.findIndex(leg => leg.legId === newLeg1.legId);
    if (legIndex > -1) {
      const newLegs = context.plan.legs.remove(legIndex).remove(legIndex).insert(legIndex, oldLeg);
      const newPlan = new Plan(context.plan.source, newLegs);
      context.updatePlan(newPlan);
    }
  }

}
