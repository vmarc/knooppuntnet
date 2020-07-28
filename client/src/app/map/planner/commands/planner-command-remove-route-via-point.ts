import {List} from "immutable";
import {PlannerContext} from "../context/planner-context";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandRemoveRouteViaPoint implements PlannerCommand {

  constructor(private readonly oldLeg: PlanLeg,
              private readonly newLeg: PlanLeg) {
  }

  public do(context: PlannerContext) {

    context.debug("PlannerCommandRemoveRouteViaPoint");

    context.markerLayer.removeFlag(this.oldLeg.viaFlag);
    context.markerLayer.removeFlag(this.oldLeg.sinkFlag);
    context.routeLayer.removePlanLeg(this.oldLeg.featureId);
    context.markerLayer.addFlag(this.newLeg.sinkFlag);
    context.routeLayer.addPlanLeg(this.newLeg);

    const newLegs: List<PlanLeg> = context.plan.legs.map(leg => leg.featureId === this.oldLeg.featureId ? this.newLeg : leg);
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {

    context.debug("PlannerCommandRemoveRouteViaPoint undo");

    context.markerLayer.removeFlag(this.newLeg.sinkFlag);
    context.routeLayer.removePlanLeg(this.newLeg.featureId);
    context.markerLayer.addFlag(this.oldLeg.viaFlag);
    context.markerLayer.addFlag(this.oldLeg.sinkFlag);
    context.routeLayer.addPlanLeg(this.oldLeg);

    const newLegs = context.plan.legs.map(leg => leg.featureId === this.newLeg.featureId ? this.oldLeg : leg);
    const newPlan = context.plan.withLegs(newLegs);
    context.updatePlan(newPlan);
  }

}
