import {List} from "immutable";
import {Plan} from "../../../kpn/api/common/planner/plan";
import {PlanLeg} from "../../../kpn/api/common/planner/plan-leg";
import {PlannerContext} from "../context/planner-context";
import {PlanFlag} from "../plan/plan-flag";
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

    context.routeLayer.removeFlag(oldLeg1.sinkNode.featureId);
    context.routeLayer.removePlanLeg(oldLeg1.featureId);
    context.routeLayer.removePlanLeg(oldLeg2.featureId);
    context.routeLayer.addPlanLeg(newLeg);

    const newLegs: List<PlanLeg> = context.plan.legs
      .map(leg => leg.featureId === oldLeg1.featureId ? newLeg : leg)
      .filter(leg => leg.featureId !== oldLeg2.featureId);
    const newPlan = new Plan(context.plan.sourceNode, newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    const oldLeg1 = context.legs.getById(this.oldLegId1);
    const oldLeg2 = context.legs.getById(this.oldLegId2);
    const newLeg = context.legs.getById(this.newLegId);

    context.routeLayer.addFlag(PlanFlag.fromViaNode(oldLeg1.sinkNode));
    context.routeLayer.addPlanLeg(oldLeg1);
    context.routeLayer.addPlanLeg(oldLeg2);
    context.routeLayer.removePlanLeg(newLeg.featureId);

    const legIndex = context.plan.legs.findIndex(leg => leg.featureId === newLeg.featureId);
    if (legIndex > -1) {
      const newLegs1 = context.plan.legs.update(legIndex, () => oldLeg1);
      const newLegs2 = newLegs1.insert(legIndex + 1, oldLeg2);
      const newPlan = new Plan(context.plan.sourceNode, newLegs2);
      context.updatePlan(newPlan);
    }
  }

}
