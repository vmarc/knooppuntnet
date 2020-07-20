import {List} from "immutable";
import {PlannerContext} from "../context/planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveRouteViaPointToNode implements PlannerCommand {

  constructor(private readonly oldLegId: string,
              private readonly newLegId1: string,
              private readonly newLegId2: string) {
  }

  public do(context: PlannerContext) {
    const oldLeg = context.legs.getById(this.oldLegId);
    const newLeg1 = context.legs.getById(this.newLegId1);
    const newLeg2 = context.legs.getById(this.newLegId2);

    context.markerLayer.removeFlag(oldLeg.viaFlag);
    context.markerLayer.addFlag(newLeg1.sinkFlag);
    context.routeLayer.removePlanLeg(oldLeg.featureId);
    context.routeLayer.addPlanLeg(newLeg1);
    context.routeLayer.addPlanLeg(newLeg2);

    const newLegs = context.plan.legs.flatMap(leg => {
      if (leg.featureId === oldLeg.featureId) {
        return List([newLeg1, newLeg2]);
      }
      return List([leg]);
    });

    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    const oldLeg = context.legs.getById(this.oldLegId);
    const newLeg1 = context.legs.getById(this.newLegId1);
    const newLeg2 = context.legs.getById(this.newLegId2);

    context.markerLayer.removeFlag(newLeg1.sinkFlag);
    context.markerLayer.addFlag(oldLeg.viaFlag);
    context.routeLayer.removePlanLeg(newLeg1.featureId);
    context.routeLayer.removePlanLeg(newLeg2.featureId);
    context.routeLayer.addPlanLeg(oldLeg);

    const newLegs = context.plan.legs.flatMap(leg => {
      if (leg.featureId === newLeg1.featureId) {
        return List([oldLeg]);
      }
      if (leg.featureId === newLeg2.featureId) {
        return List([]);
      }
      return List([leg]);
    });

    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

}
