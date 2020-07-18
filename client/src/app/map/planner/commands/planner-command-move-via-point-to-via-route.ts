import {List} from "immutable";
import {PlannerContext} from "../context/planner-context";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveViaPointToViaRoute implements PlannerCommand {

  constructor(private readonly oldLegId1: string,
              private readonly oldLegId2: string,
              private readonly newLegId1: string,
              private readonly newLegId2: string) {
  }

  public do(context: PlannerContext) {

    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg1 = context.legs.getById(this.newLegId1);
    const newLeg2 = context.legs.getById(this.newLegId2);

    context.markerLayer.removeFlag(oldLeg1.sinkFlag);
    context.markerLayer.addFlag(newLeg1.viaFlag);
    context.routeLayer.removePlanLeg(oldLeg1.featureId);
    context.routeLayer.removePlanLeg(oldLeg2.featureId);
    context.routeLayer.addPlanLeg(newLeg1);
    context.routeLayer.addPlanLeg(newLeg2);

    const newLegs: List<PlanLeg> = context.plan.legs.map(leg => {
      if (leg.featureId === oldLeg1.featureId) {
        return newLeg1;
      }
      if (leg.featureId === oldLeg2.featureId) {
        return newLeg2;
      }
      return leg;
    });
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg1 = context.legs.getById(this.newLegId1);
    const newLeg2 = context.legs.getById(this.newLegId2);

    context.markerLayer.removeFlag(newLeg1.viaFlag);

    context.markerLayer.addFlag(oldLeg1.sinkFlag);

    context.routeLayer.removePlanLeg(newLeg1.featureId);
    context.routeLayer.removePlanLeg(newLeg2.featureId);
    context.routeLayer.addPlanLeg(oldLeg1);
    context.routeLayer.addPlanLeg(oldLeg2);

    const newLegs: List<PlanLeg> = context.plan.legs.map(leg => {
      if (leg.featureId === newLeg1.featureId) {
        return oldLeg1;
      }
      if (leg.featureId === newLeg2.featureId) {
        return oldLeg2;
      }
      return leg;
    });
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

}
