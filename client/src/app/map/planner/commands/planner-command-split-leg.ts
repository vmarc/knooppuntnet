import {PlannerContext} from "../context/planner-context";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandSplitLeg implements PlannerCommand {

  constructor(private oldLegId: string,
              private newLegId1: string,
              private newLegId2: string) {
  }

  public do(context: PlannerContext) {

    const oldLeg = context.legs.getById(this.oldLegId);
    const newLeg1 = context.legs.getById(this.newLegId1);
    const newLeg2 = context.legs.getById(this.newLegId2);

    context.routeLayer.addFlag(PlanFlag.oldVia(newLeg1.sinkNode));
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

    const oldLeg = context.legs.getById(this.oldLegId);
    const newLeg1 = context.legs.getById(this.newLegId1);
    const newLeg2 = context.legs.getById(this.newLegId2);

    context.routeLayer.removeFlagWithFeatureId(newLeg1.sinkNode.featureId); // remove connection node
    context.routeLayer.removePlanLeg(newLeg1.featureId);
    context.routeLayer.removePlanLeg(newLeg2.featureId);
    context.routeLayer.addPlanLeg(oldLeg);
    const legIndex = context.plan.legs.findIndex(leg => leg.featureId === newLeg1.featureId);
    if (legIndex > -1) {
      const newLegs = context.plan.legs.remove(legIndex).remove(legIndex).insert(legIndex, oldLeg);
      const newPlan = context.plan.withLegs(newLegs);
      context.updatePlan(newPlan);
    }
  }

}
