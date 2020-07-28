import {PlannerContext} from "../context/planner-context";
import {PlannerCommand} from "./planner-command";
import {PlanLeg} from "../plan/plan-leg";

export class PlannerCommandSplitLeg implements PlannerCommand {

  constructor(private oldLeg: PlanLeg,
              private newLeg1: PlanLeg,
              private newLeg2: PlanLeg) {
  }

  public do(context: PlannerContext) {

    context.debug("PlannerCommandSplitLeg");

    context.markerLayer.removeFlag(this.oldLeg.viaFlag);
    context.markerLayer.addFlag(this.newLeg1.sinkFlag);
    const legIndex = context.plan.legs.findIndex(leg => leg.featureId === this.oldLeg.featureId);
    if (legIndex > -1) {
      context.routeLayer.removePlanLeg(this.oldLeg.featureId);
      context.routeLayer.addPlanLeg(this.newLeg1);
      context.routeLayer.addPlanLeg(this.newLeg2);
      const newLegs = context.plan.legs.remove(legIndex).push(this.newLeg1).push(this.newLeg2);
      const newPlan = context.plan.withLegs(newLegs);
      context.updatePlan(newPlan);
    }
  }

  public undo(context: PlannerContext) {

    context.debug("PlannerCommandSplitLeg undo");

    context.markerLayer.removeFlag(this.newLeg1.sinkFlag);
    context.routeLayer.removePlanLeg(this.newLeg1.featureId);
    context.routeLayer.removePlanLeg(this.newLeg2.featureId);
    context.markerLayer.addFlag(this.oldLeg.viaFlag);
    context.routeLayer.addPlanLeg(this.oldLeg);
    const legIndex = context.plan.legs.findIndex(leg => leg.featureId === this.newLeg1.featureId);
    if (legIndex > -1) {
      const newLegs = context.plan.legs.remove(legIndex).remove(legIndex).insert(legIndex, this.oldLeg);
      const newPlan = context.plan.withLegs(newLegs);
      context.updatePlan(newPlan);
    }
  }

}
