import {PlannerContext} from "../context/planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddLeg implements PlannerCommand {

  constructor(private legId: string) {
  }

  public do(context: PlannerContext) {

    let newLegs = context.plan.legs;

    const lastLeg = newLegs.last(null);
    if (lastLeg !== null) {
      const updatedLastLegSinkFlag = lastLeg.viaFlag === null ? lastLeg.sinkFlag.toVia() : lastLeg.sinkFlag.toInvisible();
      const updatedLastLeg = lastLeg.withSinkFlag(updatedLastLegSinkFlag);
      context.legs.add(updatedLastLeg);
      newLegs = newLegs.set(newLegs.size - 1, updatedLastLeg);
      context.markerLayer.updateFlag(updatedLastLeg.sinkFlag);
    }

    const leg = context.legs.getById(this.legId);
    newLegs = newLegs.push(leg);

    context.markerLayer.addFlag(leg.viaFlag);
    context.markerLayer.addFlag(leg.sinkFlag);
    context.routeLayer.addPlanLeg(leg);

    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    const leg = context.legs.getById(this.legId);

    context.routeLayer.removePlanLeg(this.legId);
    context.markerLayer.removeFlag(leg.viaFlag);
    context.markerLayer.removeFlag(leg.sinkFlag);

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
