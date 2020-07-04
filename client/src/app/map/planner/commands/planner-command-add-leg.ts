import {PlannerContext} from "../context/planner-context";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddLeg implements PlannerCommand {

  constructor(private legId: string) {
  }

  public do(context: PlannerContext) {

    let newLegs = context.plan.legs;

    const lastLeg = newLegs.last(null);
    if (lastLeg !== null) {
      const newSinkFlag = lastLeg.viaFlag === null ? lastLeg.sinkFlag.toVia() : lastLeg.sinkFlag.toInvisible();
      const updatedLastLeg = lastLeg.withSinkFlag(newSinkFlag);
      newLegs = newLegs.set(newLegs.size - 1, updatedLastLeg);
      context.routeLayer.updateFlag(updatedLastLeg.sinkFlag);
    }

    const leg = context.legs.getById(this.legId);
    newLegs = newLegs.push(leg);

    context.routeLayer.addFlag(leg.viaFlag);
    context.routeLayer.addFlag(leg.sinkFlag);
    context.routeLayer.addPlanLeg(leg);

    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    const leg = context.legs.getById(this.legId);

    context.routeLayer.removePlanLeg(this.legId);
    context.routeLayer.removeFlag(leg.viaFlag);
    context.routeLayer.removeFlag(leg.sinkFlag);

    let newLegs = context.plan.legs.slice(0, -1);

    const lastLeg = newLegs.last(null);
    if (lastLeg !== null) {
      const updatedLastLeg = lastLeg.withSinkFlag(lastLeg.sinkFlag.toEnd());
      newLegs = newLegs.set(newLegs.size - 1, updatedLastLeg);
      context.routeLayer.updateFlag(updatedLastLeg.sinkFlag);
    }

    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

}
