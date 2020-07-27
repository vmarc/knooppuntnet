import {PlannerContext} from "../context/planner-context";
import {PlannerCommand} from "./planner-command";
import {List} from "immutable";

export class PlannerCommandRemoveViaPoint implements PlannerCommand {

  constructor(private readonly oldLegId1: string,
              private readonly oldLegId2: string,
              private readonly newLegId: string) {
  }

  public do(context: PlannerContext) {

    context.debug("PlannerCommandRemoveViaPoint");

    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg = context.legs.getById(this.newLegId);

    context.markerLayer.removeFlag(oldLeg1.viaFlag);
    context.markerLayer.removeFlag(oldLeg1.sinkFlag);
    context.routeLayer.removePlanLeg(oldLeg1.featureId);

    context.markerLayer.removeFlag(oldLeg2.viaFlag);
    context.markerLayer.removeFlag(oldLeg2.sinkFlag);
    context.routeLayer.removePlanLeg(oldLeg2.featureId);

    context.markerLayer.addFlag(newLeg.viaFlag);
    context.markerLayer.addFlag(newLeg.sinkFlag);
    context.routeLayer.addPlanLeg(newLeg);

    const newLegs = context.plan.legs
      .map(leg => leg.featureId === oldLeg1.featureId ? newLeg : leg)
      .filterNot(leg => leg.featureId === oldLeg2.featureId);
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    context.debug("PlannerCommandRemoveViaPoint undo");

    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg = context.legs.getById(this.newLegId);

    context.markerLayer.removeFlag(newLeg.sinkFlag);
    context.routeLayer.removePlanLeg(newLeg.featureId);

    context.markerLayer.addFlag(oldLeg1.viaFlag);
    context.markerLayer.addFlag(oldLeg1.sinkFlag);
    context.routeLayer.addPlanLeg(oldLeg1);

    context.markerLayer.addFlag(oldLeg2.viaFlag);
    context.markerLayer.addFlag(oldLeg2.sinkFlag);
    context.routeLayer.addPlanLeg(oldLeg2);

    const newLegs = context.plan.legs.flatMap(leg => {
      if (leg.featureId === newLeg.featureId) {
        return List([oldLeg1, oldLeg2]);
      }
      return List([leg]);
    });
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

}
