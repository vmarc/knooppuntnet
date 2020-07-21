import {PlannerContext} from "../context/planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandSplitLeg implements PlannerCommand {

  constructor(private oldLegId: string,
              private newLegId1: string,
              private newLegId2: string) {
  }

  public do(context: PlannerContext) {

    context.debug("PlannerCommandSplitLeg");

    const oldLeg = context.legs.getById(this.oldLegId);
    const newLeg1 = context.legs.getById(this.newLegId1);
    const newLeg2 = context.legs.getById(this.newLegId2);

    context.markerLayer.removeFlag(oldLeg.viaFlag);
    context.markerLayer.addFlag(newLeg1.sinkFlag);
    const legIndex = context.plan.legs.findIndex(leg => leg.featureId === oldLeg.featureId);
    if (legIndex > -1) {
      context.routeLayer.removePlanLeg(oldLeg.featureId);
      context.routeLayer.addPlanLeg(newLeg1);
      context.routeLayer.addPlanLeg(newLeg2);
      const newLegs = context.plan.legs.remove(legIndex).push(newLeg1).push(newLeg2);
      const newPlan = context.plan.withLegs(newLegs);
      context.updatePlan(newPlan);
    }
  }

  public undo(context: PlannerContext) {

    context.debug("PlannerCommandSplitLeg undo");

    const oldLeg = context.legs.getById(this.oldLegId);
    const newLeg1 = context.legs.getById(this.newLegId1);
    const newLeg2 = context.legs.getById(this.newLegId2);

    context.markerLayer.removeFlag(newLeg1.sinkFlag);
    context.routeLayer.removePlanLeg(newLeg1.featureId);
    context.routeLayer.removePlanLeg(newLeg2.featureId);
    context.markerLayer.addFlag(oldLeg.viaFlag);
    context.routeLayer.addPlanLeg(oldLeg);
    const legIndex = context.plan.legs.findIndex(leg => leg.featureId === newLeg1.featureId);
    if (legIndex > -1) {
      const newLegs = context.plan.legs.remove(legIndex).remove(legIndex).insert(legIndex, oldLeg);
      const newPlan = context.plan.withLegs(newLegs);
      context.updatePlan(newPlan);
    }
  }

}
