import {List} from "immutable";
import {PlannerContext} from "../context/planner-context";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandRemoveViaPoint implements PlannerCommand {

  constructor(private readonly oldLegId1: string,
              private readonly oldLegId2: string,
              private readonly newLegId: string) {
  }

  public do(context: PlannerContext) {

    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg = context.legs.getById(this.newLegId);

    context.markerLayer.removeFlag(oldLeg1.sinkFlag);
    context.markerLayer.removeFlag(oldLeg2.sinkFlag);
    context.markerLayer.removeFlag(oldLeg1.viaFlag);
    context.markerLayer.removeFlag(oldLeg2.viaFlag);
    context.markerLayer.addFlag(newLeg.sinkFlag);
    context.markerLayer.addFlag(newLeg.viaFlag);
    context.routeLayer.removePlanLeg(oldLeg1.featureId);
    context.routeLayer.removePlanLeg(oldLeg2.featureId);
    context.routeLayer.addPlanLeg(newLeg);

    const newLegs: List<PlanLeg> = context.plan.legs
      .map(leg => leg.featureId === oldLeg1.featureId ? newLeg : leg)
      .filterNot(leg => leg.featureId === oldLeg2.featureId);
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg = context.legs.getById(this.newLegId);

    context.markerLayer.removeFlag(newLeg.sinkFlag);
    context.markerLayer.addFlag(oldLeg1.sinkFlag);
    context.markerLayer.addFlag(oldLeg2.sinkFlag);
    context.markerLayer.addFlag(oldLeg1.viaFlag);
    context.markerLayer.addFlag(oldLeg2.viaFlag);
    context.routeLayer.removePlanLeg(newLeg.featureId);
    context.routeLayer.addPlanLeg(oldLeg1);
    context.routeLayer.addPlanLeg(oldLeg2);

    const legIndex = context.plan.legs.findIndex(leg => leg.featureId === newLeg.featureId);
    if (legIndex > -1) {
      const newLegs1 = context.plan.legs.update(legIndex, () => oldLeg1);
      const newLegs2 = newLegs1.insert(legIndex + 1, oldLeg2);
      const newPlan = context.plan.withLegs(newLegs2);
      context.updatePlan(newPlan);
    }
  }

}
