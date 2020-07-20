import {List} from "immutable";
import {PlannerContext} from "../context/planner-context";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandRemoveRouteViaPoint implements PlannerCommand {

  constructor(private readonly oldLegId: string,
              private readonly newLegId: string) {
  }

  public do(context: PlannerContext) {

    const oldLeg = context.legs.getById(this.oldLegId);
    const newLeg = context.legs.getById(this.newLegId);

    context.markerLayer.removeFlag(oldLeg.viaFlag);
    context.markerLayer.removeFlag(oldLeg.sinkFlag);
    context.routeLayer.removePlanLeg(oldLeg.featureId);
    context.markerLayer.addFlag(newLeg.sinkFlag);
    context.routeLayer.addPlanLeg(newLeg);

    const newLegs: List<PlanLeg> = context.plan.legs.map(leg => leg.featureId === oldLeg.featureId ? newLeg : leg);
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    const oldLeg = context.legs.getById(this.oldLegId);
    const newLeg = context.legs.getById(this.newLegId);

    context.markerLayer.removeFlag(newLeg.sinkFlag);
    context.routeLayer.removePlanLeg(newLeg.featureId);
    context.markerLayer.addFlag(oldLeg.viaFlag);
    context.markerLayer.addFlag(oldLeg.sinkFlag);
    context.routeLayer.addPlanLeg(oldLeg);

    const newLegs: List<PlanLeg> = context.plan.legs.map(leg => leg.featureId === newLeg.featureId ? oldLeg : leg);
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

}
