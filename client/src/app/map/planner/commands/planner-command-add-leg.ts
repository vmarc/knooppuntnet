import {PlannerContext} from "../context/planner-context";
import {PlannerCommand} from "./planner-command";
import {PlanLeg} from "../plan/plan-leg";

export class PlannerCommandAddLeg implements PlannerCommand {

  constructor(private leg: PlanLeg) {
  }

  public do(context: PlannerContext) {

    context.debug("PlannerCommandAddLeg");

    let newLegs = context.plan.legs;
    const lastLeg = newLegs.last(null);
    if (lastLeg !== null) {
      const updatedLastLegSinkFlag = lastLeg.viaFlag === null ? lastLeg.sinkFlag.toVia() : lastLeg.sinkFlag.toInvisible();
      const updatedLastLeg = lastLeg.withSinkFlag(updatedLastLegSinkFlag);
      context.legs.add(updatedLastLeg);
      newLegs = newLegs.set(newLegs.size - 1, updatedLastLeg);
      context.markerLayer.updateFlag(updatedLastLeg.sinkFlag);
    }

    newLegs = newLegs.push(this.leg);

    context.markerLayer.addFlag(this.leg.viaFlag);
    context.markerLayer.addFlag(this.leg.sinkFlag);
    context.routeLayer.addPlanLeg(this.leg);

    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    context.debug("PlannerCommandAddLeg undo");

    context.routeLayer.removePlanLeg(this.leg.featureId);
    context.markerLayer.removeFlag(this.leg.viaFlag);
    context.markerLayer.removeFlag(this.leg.sinkFlag);

    let newLegs = context.plan.legs.slice(0, -1);

    const lastLeg = newLegs.last(null);
    if (lastLeg !== null) {
      const updatedLastLeg = lastLeg.withSinkFlag(lastLeg.sinkFlag.toEnd());
      context.legs.add(updatedLastLeg);
      newLegs = newLegs.set(newLegs.size - 1, updatedLastLeg);
      context.markerLayer.updateFlag(updatedLastLeg.sinkFlag);
    }

    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

}
