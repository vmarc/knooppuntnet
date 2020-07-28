import {List} from "immutable";
import {PlannerContext} from "../context/planner-context";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandMoveViaPointToViaRoute implements PlannerCommand {

  constructor(private readonly oldLeg1: PlanLeg,
              private readonly oldLeg2: PlanLeg,
              private readonly newLeg1: PlanLeg,
              private readonly newLeg2: PlanLeg) {
  }

  public do(context: PlannerContext) {

    context.debug("PlannerCommandMoveViaPointToViaRoute");

    context.markerLayer.removeFlag(this.oldLeg1.sinkFlag);
    context.markerLayer.addFlag(this.newLeg1.viaFlag);
    context.routeLayer.removePlanLeg(this.oldLeg1.featureId);
    context.routeLayer.removePlanLeg(this.oldLeg2.featureId);
    context.routeLayer.addPlanLeg(this.newLeg1);
    context.routeLayer.addPlanLeg(this.newLeg2);

    const newLegs: List<PlanLeg> = context.plan.legs.map(leg => {
      if (leg.featureId === this.oldLeg1.featureId) {
        return this.newLeg1;
      }
      if (leg.featureId === this.oldLeg2.featureId) {
        return this.newLeg2;
      }
      return leg;
    });
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    context.debug("PlannerCommandMoveViaPointToViaRoute undo");

    context.markerLayer.removeFlag(this.newLeg1.viaFlag);

    context.markerLayer.addFlag(this.oldLeg1.sinkFlag);

    context.routeLayer.removePlanLeg(this.newLeg1.featureId);
    context.routeLayer.removePlanLeg(this.newLeg2.featureId);
    context.routeLayer.addPlanLeg(this.oldLeg1);
    context.routeLayer.addPlanLeg(this.oldLeg2);

    const newLegs: List<PlanLeg> = context.plan.legs.map(leg => {
      if (leg.featureId === this.newLeg1.featureId) {
        return this.oldLeg1;
      }
      if (leg.featureId === this.newLeg2.featureId) {
        return this.oldLeg2;
      }
      return leg;
    });
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

}
